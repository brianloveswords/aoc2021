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
with

draws as (
  select row_number() over () as id, draw 
  --from unnest([7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1]) as draw
  from unnest([7,4,9,5]) as draw
)

, rounds as (
  select id as roundNo, array_agg(draw) over (order by id) as draws
  from draws
)

, rawBoards as (
  select 1 as id, [
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
  select id, array_agg(openCell(cell)) as board
  from rawBoards, unnest(board) as cell
  group by 1
)

select 
  id
  , roundNo
  , draws
  , array_reverse(draws)[offset(0)] as lastDraw
  , playRound(draws, board) as board
from 
  startBoards
  , rounds
where id = 1
```
