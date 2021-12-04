```sql
create temp function playRound(
    draws array<int>
  , board array<struct<tag string, val int>>
) returns array<struct<tag string, val int>> as ((
  /* 
    break the board into cells, then check if each cell is in the set of draws
    that have been called. if it is, change it to the "marked" variant.
  */
  select array_agg(
    case when cell.val in unnest(draws) 
      then struct("marked", cell.val)
      else cell 
    end
  )
  from unnest(board) as cell
));

create temp function allMarked(
  cells array<struct<tag string, val int>>
) returns boolean as (
  array_length(cells) = (
    select count(1) 
    from unnest(cells) as cell 
    where cell.tag = "marked"
  )
);

create temp function lookupLocation(
  board array<struct<tag string, val int>>
  , loc struct<x int, y int>
) returns struct<tag string, val int> as (
  /* 
    lookup a cell from a board using (x,y) coordinates. board is stored as a
    linear array, so we need to translate (x,y) into a linear index. we can
    use """math""" for this: the y coordinate can act as a scaling factor for
    the x coordinate to give us the linear index based on the width of the
    board always being 5.
  */
  board[ordinal(loc.x + (loc.y - 1) * 5)] 
);


create temp function getCells(
  board array<struct<tag string, val int>>
  , locs array<struct<x int, y int>>
) returns array<struct<tag string, val int>> as ((
  /* 
    given a board and an array of locations, return the cells at those
    locations.
  */
  select array_agg(lookupLocation(board, loc)) 
  from unnest(locs) as loc
));

create temp function hasBingo(
  board array<struct<tag string, val int>>
) returns boolean as (
  false
  -- horizontal wins
  or allMarked(getCells(board, [(1,1), (1,2), (1,3), (1,4), (1,5)]))
  or allMarked(getCells(board, [(2,1), (2,2), (2,3), (2,4), (2,5)]))
  or allMarked(getCells(board, [(3,1), (3,2), (3,3), (3,4), (3,5)]))
  or allMarked(getCells(board, [(4,1), (4,2), (4,3), (4,4), (4,5)]))
  or allMarked(getCells(board, [(5,1), (5,2), (5,3), (5,4), (5,5)]))

  -- vertical wins
  or allMarked(getCells(board, [(1,1), (2,1), (3,1), (4,1), (5,1)]))
  or allMarked(getCells(board, [(1,2), (2,2), (3,2), (4,2), (5,2)]))
  or allMarked(getCells(board, [(1,3), (2,3), (3,3), (4,3), (5,3)]))
  or allMarked(getCells(board, [(1,4), (2,4), (3,4), (4,4), (5,4)]))
  or allMarked(getCells(board, [(1,5), (2,5), (3,5), (4,5), (5,5)]))
);

create temp function calcScore(
  board array<struct<tag string, val int>>
  , lastDraw int
) returns int as (
  /*
    calculate the score for a given board. the score is the product of the
    last drawn number and the sum of the values of open cells.
  */ 
  lastDraw * (
    select sum(cell.val) 
    from unnest(board) as cell 
    where cell.tag = "open"
  )
);

with
draws as (
  /* unnest will split the array apart and we'll get 1 row per entry */
  select row_number() over () as id, draw 
  from unnest([7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1]) as draw
)

, rounds as (
  /* 
    we use `array_agg` in analytic function form to create sets that include
    all draws made up to a certain point; e.g. this will create the set
      - 1, [7]
      - 2, [7, 4]
      - 3, [7, 4, 9]
    ...etc
  */
  select id as roundNo
  , array_agg(draw) over (order by id) as draws
  from draws
)
, startBoards as (
  /* 
    boards will be represented as a 1-dimensional array of cells. we create
    them as ints first, then turn them into a tagged union, starting each cell
    as "open".
  */
  with base as (
    select 1 as boardId, [
      22, 13, 17, 11,  0, 
       8,  2, 23,  4, 24,
      21,  9, 14, 16,  7,
       6, 10,  3, 18,  5,
       1, 12, 20, 15, 19
    ] as board
    union all select 2, [
       3, 15,  0,  2, 22,
       9, 18, 13, 17,  5,
      19,  8,  7, 25, 23,
      20, 11, 10, 24,  4,
      14, 21, 16, 12,  6
    ]
    union all select 3, [
      14, 21, 17, 24,  4,
      10, 16, 15,  9, 19,
      18,  8, 23, 26, 20,
      22, 11, 13,  6,  5,
       2,  0, 12,  3,  7
    ]
  )
  select boardId, array_agg(struct("open" as tag, cell)) as board
  from base, unnest(board) as cell
  group by 1
)


, played as (
  /* play each round! */
  select 
      boardId
    , roundNo
    , draws
    , array_reverse(draws)[offset(0)] as lastDraw
    , playRound(draws, board) as board
  from startBoards, rounds
)


, winners as (
  /*
    select only the rounds with winning boards and give them sequence IDs
    partitioned by each winning board, then select just the first round for
    each winning board.
  */
  select * except(seqId) 
  from (
    select 
        boardId
      , roundNo
      , calcScore(board, lastDraw) as score
      , draws
      , lastDraw
      , board
      , row_number() over (partition by boardId order by roundNo) as seqId
    from played
    where hasBingo(board)
  )
  where seqId = 1
)

select * 
from winners
order by roundNo```
# scratch

```sql
/* debugDraws as (
  select row_number() over () as id, draw 
  from unnest([1,6,11,16,21,2,3,4,5]) as draw
)
, debugRounds as (
  select id as roundNo, array_agg(draw) over (order by id) as draws
  from debugDraws
)
, debugBoard as (
  select 1 as boardId, array_agg(openCell(cell)) as board
  from (
    select [
       1,  2,  3,  4,  5,
       6,  7,  8,  9, 10,
      11, 12, 13, 14, 15,
      16, 17, 18, 19, 20,
      21, 22, 23, 24, 25
    ] as board
  ), unnest(board) as cell
)
, debugPlayed as (
  select 
    boardId
    , roundNo
    , draws
    , array_reverse(draws)[offset(0)] as lastDraw
    , playRound(draws, board) as board
  from debugBoard, debugRounds
)
, debugWinners as (
  select * except(seqId) from (
      select boardId
      , roundNo
      , score(board, lastDraw) as score
      , draws
      , lastDraw
      , board
      , row_number() over (partition by boardId order by roundNo) as seqId
    from debugPlayed

  )
)
*/
select 1```
