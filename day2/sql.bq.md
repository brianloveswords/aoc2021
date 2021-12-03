## part 1
```sql
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
  /* 
    the struct isn't strictly necessary, but prevents me from having to
    repeat the case statement for each component 
  */
  select (
    case 
      when cmd = "forward" then struct(val as x, 0 as y)
      when cmd = "up" then (0, -val)
      when cmd = "down" then (0, val)
      else (0, 0)
    end
  ).*
  from example
)

select sum(x) * sum(y) as result from positions
```
## part 2
```sql
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
)

, commands as (
  /*
    transform input into commands. `up` and `down` are only different in sign,
    so we combine them in to `aim`, and we'll call forward `thrust`.
  */
  select i, (
    case cmd
      when "forward" then struct("thrust" as tag, val as value)
      when "up" then ("aim", -val)
      when "down" then ("aim", val)
      else error("don't know what to do with command: " || cmd)
    end
  ).*
  from input
)

, stage0 as (
  /*
    we can calculate the running horizontal value at this point since it is
    independent of any `aim` commands. we'll also keep a running total of the
    aim values.
  */  
  select i
    , tag
    , value
    , sum(if (tag = 'thrust', value, 0)) over (order by i) as running_horizontal
    , sum(if (tag = 'aim', value, 0)) over (order by i) as running_aim
  from commands
  order by i
)

, stage1 as (
  /*
    now that we have the running total of the `aim`, which represents the aim
    at the time of any given command, we don't need the `aim` rows
    anymore—we'll have the aim at time of thrust in the `running_aim` column.
  */
  select * from stage0
  where tag = 'thrust'
)

, stage2 as (
  /*
    now we can calculate incremental changes to depth by taking the product of
    the thrust value with the aim at the time of thrust.
  */
  select i
    , value 
    , running_horizontal
    , running_aim
    , value * running_aim as incremental_depth
  from stage1
)

, stage3 as (
  /* 
    final product will be the maximum running horizontal, and the sum of all
    the incremental depth changes.
  */
  select 
    max(running_horizontal) as horizontal
    , sum(incremental_depth) as depth
  from stage2
)

select horizontal * depth as result from stage3```
