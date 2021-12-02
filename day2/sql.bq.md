## part 1
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
## part 2
```sql
create temp function command(tag string, value int) 
returns struct<tag string, value int> as (
  struct(tag, value)
);

with 

example as (
  select 1 as i, "forward" as cmd, 5 as val
  union all select 2, "down", 5
  union all select 3, "forward", 8
  union all select 4, "up", 3
  union all select 5, "down", 8
  union all select 6, "forward", 2
)

, input as (
   select * from example 
   -- select * from temp.aoc_day2
)

, commands as (
  select i, (
    case cmd
      when "forward" then command("thrust", val)
      when "up" then command("aim", -val)
      when "down" then command("aim", val)
      else error("don't know what to do with command: " || cmd)
    end
  ).*
  from input
)

, stage0 as (
  -- add flux: if the previous row's command is not the same as the current
  -- row's command, flux is 1. we can sum this in the next stage to identify
  -- sequences of the same action so we can flatten them into a single command
  select i
    , tag
    , value
    , if (tag = lag(tag) over (order by i), 0, 1) as flux
  from commands
)

, stage1 as (
  -- generate sequence ID by summing fluxes
  select i
    , tag
    , value
    , sum(flux) over (order by i) as seq_id
  from stage0
)

, stage2 as (
  -- combine sequences into a single command  
  select max(i) as i
    , tag
    , sum(value) as value
  from stage1
  group by tag, seq_id
)

, stage3 as (
  -- at this point we can calculate horizontal since it is independent of any
  -- `aim` commands. we keep a running total of the aim values, then in the
  -- next stage we can throw out the `aim` rows and just keep the `thrust`
  -- which will have a `running_aim` of what the aim was at the time of the
  -- thrust.
  select i
    , tag
    , value
    , sum(if (tag = 'thrust', value, 0)) over (
      order by i
    ) as running_horizontal
    , sum(if (tag = 'aim', value, 0)) over (
      order by i
    ) as running_aim
  from stage2
  order by i
)

, stage4 as (
  select * from stage3 
  where tag = 'thrust'
)

, stage5 as (
  -- now we can calculate incremental changes to depth by taking the product
  -- of the thrust value with the aim at the time of thrust.
  select i
    , value 
    , running_horizontal
    , running_aim
    , value * running_aim as incremental_depth
  from stage4
)

, stage6 as (
  -- final product will be the maximum running horizontal, and the sum of all
  -- the incremental depth changes.
  select 
    max(running_horizontal) as horizontal
    , sum(incremental_depth) as depth
  from stage5
)

select horizontal * depth from stage6```
