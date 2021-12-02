```sql
with example as (
  select 1 as i, 199 as x
  union all select 2, 200
  union all select 3, 208
  union all select 4, 210
  union all select 5, 200
  union all select 6, 207
  union all select 7, 240
  union all select 8, 269
  union all select 9, 260
  union all select 10, 263
)

, part1 as (
  with base as (
    select
      lag(x) over (order by i) as prev
      , x as curr
    from example
  )
  , flux as  (
    select
      case
        when prev is null then 0
        when curr > prev then 1
        else 0
      end as inc
    from base
  )

  select sum(inc) as increases
  from flux
)

, part2 as (
  /*
    we can get a sliding window by using `lag` and then filtering where none
    of the lagged values are null. once we have that, we can then sum up the
    values in the window then use the same procedure as part1.
  */
  with windowed as (
    select i
      , _1 + _2 + _3 as x
    from (
      select i
        , lag(x, 2) over (order by i) as _1
        , lag(x, 1) over (order by i) as _2
        , lag(x, 0) over (order by i) as _3
      from example
    )
    where true
      and _1 is not null
      and _2 is not null
      and _3 is not null
  )

  -- same as part1 from here out
  , base as (
    select
      lag(x) over (order by i) as prev
      , x as curr
    from windowed
  )

  , flux as  (
    select
      case
        when prev is null then 0
        when curr > prev then 1
        else 0
      end as inc
    from base
  )

  select sum(inc) as increases
  from flux
)

select 'part1' as name, increases from part1
union all select 'part2' as name, increases from part2
```
