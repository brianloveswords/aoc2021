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

, diff1 as (
  with base as (
    select lag(x) over (order by i) as prev, x as curr
    from example
  ), flux as  (
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

, diff3 as (
  with stage0 as (
    select i
      , lag(x, 2) over (order by i) as _1
      , lag(x, 1) over (order by i) as _2
      , x as _3
    from example
  )

  , stage1 as (
    select i, _1 + _2 + _3 as x
    from stage0
    where _1 is not null and _2 is not null and _3 is not null
  )

  , base as (
    select lag(x) over (order by i) as prev, x as curr
    from stage1
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

select '1' as windowSize, * from diff1
union all select '3' as windowSize, * from diff3
