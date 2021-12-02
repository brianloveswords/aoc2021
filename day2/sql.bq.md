```sql
create temp function pos(x int, y int) 
returns struct<x int, y int> as (
  struct(x, y)
);

with 

example as (
  select "forward" as cmd, 5 as val
  union all select "down", 5
  union all select "forward", 8
  union all select "up", 3
  union all select "down", 8
  union all select "forward", 2
)

, positions as (
  select (
    case 
      when cmd = "forward" then pos(val, 0)
      when cmd = "up" then pos(0, -val)
      when cmd = "down" then pos(0, val)
      else pos(0, 0)
    end
  ).*
  from temp.aoc_day2
)

, aim as (
  select pos(0, 0).* from positions
)


select sum(x) * sum(y) from positions
```
