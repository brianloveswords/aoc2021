```sql
with example as (
  select 0 as a, 0 as b, 1 as c, 0 as d, 0 as e
  union all select 1,1,1,1,0
  union all select 1,0,1,1,0
  union all select 1,0,1,1,1
  union all select 1,0,1,0,1
  union all select 0,1,1,1,1
  union all select 0,0,1,1,1
  union all select 1,1,1,0,0
  union all select 1,0,0,0,0
  union all select 1,1,0,0,1
  union all select 0,0,0,1,0
  union all select 0,1,0,1,0
)

, bitsToInt as (
  with base as (select 0 as x union all select 1)
  select 
      b1.x as a
    , b2.x as b
    , b3.x as c
    , b4.x as d
    , b5.x as e
    , (row_number() over ()) - 1 as value
  from base as b1
    cross join base as b2
    cross join base as b3
    cross join base as b4
    cross join base as b5
)

, gamma as (
  with base as (
    select 
        approx_top_count(a, 1)[offset(0)].value as a
      , approx_top_count(b, 1)[offset(0)].value as b
      , approx_top_count(c, 1)[offset(0)].value as c
      , approx_top_count(d, 1)[offset(0)].value as d
      , approx_top_count(e, 1)[offset(0)].value as e
    from example
  )
  select value
  from base as t1
    inner join bitsToInt as t2
      on t1.a = t2.a
      and t1.b = t2.b
      and t1.c = t2.c
      and t1.d = t2.d
      and t1.e = t2.e
)

, epsilon as (
  with base as (
    select 
        approx_top_count(a, 2)[offset(1)].value as a
      , approx_top_count(b, 2)[offset(1)].value as b
      , approx_top_count(c, 2)[offset(1)].value as c
      , approx_top_count(d, 2)[offset(1)].value as d
      , approx_top_count(e, 2)[offset(1)].value as e
    from example
  )
  select value
  from base as t1
    inner join bitsToInt as t2
      on t1.a = t2.a
      and t1.b = t2.b
      and t1.c = t2.c
      and t1.d = t2.d
      and t1.e = t2.e
)

select 
  gamma.value as gamma
  , epsilon.value as epsilon
  , gamma.value * epsilon.value as result
from gamma, epsilon```
