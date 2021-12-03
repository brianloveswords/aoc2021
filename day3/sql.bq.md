```sql
/** helper functions to avoid repeating cases */
create temp function mostCommon(summed int, mid int, tiebreaker int)
returns int as (
  case 
    when summed = mid then tiebreaker
    when summed > mid then 1
    else 0
  end
);

create temp function leastCommon(summed int, mid int, tiebreaker int)
returns int as (
  case 
    when summed = mid then tiebreaker
    when summed > mid then 0
    else 1
  end
);

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
  /*
    create a lookup table for converting bits to an integer. the cross product
    will be generated deterministically starting with 0s then 1s so we can do
    a straight row numbering (minus 1) to create the (a,b,c,d,e) -> int mapping.
  */
  with base as (select 0 as x union all select 1)
  select 
      b1.x as a
    , b2.x as b
    , b3.x as c
    , b4.x as d
    , b5.x as e
    , (row_number() over ()) - 1 as intValue
  from base as b1
    cross join base as b2
    cross join base as b3
    cross join base as b4
    cross join base as b5
)

, mid as (
  select cast(floor(count(1) / 2) as int64) as value from example
)

, gamma as (
  with base as (
    /*
      bits are 1 or 0. if we sum up the bit values, and the value is greater
      than half the length of the total set, then we know the most common
      value is 1 since only 1-bits contribute to the count.
    */
    select
        mostCommon(sum(a), any_value(mid.value), 1) as a
      , mostCommon(sum(b), any_value(mid.value), 1) as b
      , mostCommon(sum(c), any_value(mid.value), 1) as c
      , mostCommon(sum(d), any_value(mid.value), 1) as d
      , mostCommon(sum(e), any_value(mid.value), 1) as e
    from example, mid
  )
  select intValue
  from base as t1
    /*
      I think of this join kinda like calling a function in an traditional
      language: `intValue = bitsToInt(a,b,c,d,e)`
    */
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
        leastCommon(sum(a), any_value(mid.value), 0) as a
      , leastCommon(sum(b), any_value(mid.value), 0) as b
      , leastCommon(sum(c), any_value(mid.value), 0) as c
      , leastCommon(sum(d), any_value(mid.value), 0) as d
      , leastCommon(sum(e), any_value(mid.value), 0) as e
    from example, mid
  )
  select intValue
  from base as t1
    inner join bitsToInt as t2
      on t1.a = t2.a
      and t1.b = t2.b
      and t1.c = t2.c
      and t1.d = t2.d
      and t1.e = t2.e
)

, final as (
  select 
    gamma.intValue as gamma
    , epsilon.intValue as epsilon
    , gamma.intValue * epsilon.intValue as result
  from gamma, epsilon
)

select * from final```
