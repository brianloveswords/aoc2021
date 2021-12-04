```sql
create temp function openCell(val int) as ( struct("open" as tag, val) );

create temp function playDraw(draw int, cell struct<tag string, val int>) as (
  case when draw != cell.val then cell
  else struct("mark", draw)
  end
);

create temp function playRound(
    draws array<int>
  , board array<struct<tag string, val int>>
) returns array<struct<tag string, val int>> as ((
  select array_agg(if (cell.val in unnest(draws), struct("mark", cell.val), cell))
  from unnest(board) as cell
));

create temp function isMarked(cell struct<tag string, val int>) as ( cell.tag = "mark" );

create temp function allMarked(cells array<struct<tag string, val int>>) as (
  (select count(1) from unnest(cells) as cell where isMarked(cell)) = array_length(cells)
);

create temp function pos(
  b array<struct<tag string, val int>>
  , l struct<x int, y int>
) as ( b[ordinal(l.x*l.y)] );


create temp function getCells(
  b array<struct<tag string, val int>>
  , ls array<struct<x int, y int>>
) as ( 
  (select array_agg(pos(b, l)) from unnest(ls) as l)
);

create temp function isWon(
  board array<struct<tag string, val int>>
) returns boolean as (
  false
  -- vertical wins
  or allMarked(getCells(board, [(1,1), (1,2), (1,3), (1,4), (1,5)]))
  or allMarked(getCells(board, [(2,1), (2,2), (2,3), (2,4), (2,5)]))
  or allMarked(getCells(board, [(3,1), (3,2), (3,3), (3,4), (3,5)]))
  or allMarked(getCells(board, [(4,1), (4,2), (4,3), (4,4), (4,5)]))
  or allMarked(getCells(board, [(5,1), (5,2), (5,3), (5,4), (5,5)]))

  -- horizontal wins
  or allMarked(getCells(board, [(1,1), (2,1), (3,1), (4,1), (5,1)]))
  or allMarked(getCells(board, [(1,2), (2,2), (3,2), (4,2), (5,2)]))
  or allMarked(getCells(board, [(1,3), (2,3), (3,3), (4,3), (5,3)]))
  or allMarked(getCells(board, [(1,4), (2,4), (3,4), (4,4), (5,4)]))
  or allMarked(getCells(board, [(1,5), (2,5), (3,5), (4,5), (5,5)]))
);

create temp function score(
  board array<struct<tag string, val int>>
  , lastDraw int
) returns int as (
  case when not isWon(board) then 0
  else lastDraw * (select sum(cell.val) from unnest(board) as cell where not isMarked(cell))
  end
);


with

draws as (
  select row_number() over () as id, draw 
  from unnest([7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1]) as draw
)

, rounds as (
  select id as roundNo, array_agg(draw) over (order by id) as draws
  from draws
)

, rawBoards as (
  select 1 as boardId, [
     22, 13, 17, 11,  0, 
      8,  2, 23,  4, 24,
     21,  9, 14, 16,  7,
      6, 10,  3, 18,  5,
      1, 12, 20, 15, 19] as board
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

, startBoards as (
  select boardId, array_agg(openCell(cell)) as board
  from rawBoards, unnest(board) as cell
  group by 1
)


, played as (
  select 
    boardId
    , roundNo
    , draws
    , array_reverse(draws)[offset(0)] as lastDraw
    , playRound(draws, board) as board
  from startBoards, rounds
)


, winners as (
  select * except(seqId) from (
      select boardId
      , roundNo
      , score(board, lastDraw) as score
      , draws
      , lastDraw
      , board
      , row_number() over (partition by boardId order by roundNo) as seqId
    from played
    where isWon(board)
  )
  where seqId = 1
)

, debugDraws as (
  select row_number() over () as id, draw 
  from unnest([1,6,11,16,21,13]) as draw
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
select * from debugWinners
```
