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

, oxygen as (
  with 
  aa as (
    select * from example
    where a = (select mostCommon(sum(a), any_value(mid.value), 1) from example, mid)
  ),
  bb as (
    select * from aa
    where b = (select mostCommon(sum(b), any_value(mid.value), 1) from aa, mid)
  ),
  cc as (
    select * from bb
    where c = (select mostCommon(sum(c), any_value(mid.value), 1) from bb, mid)
  ),
  dd as (
    select * from cc
    where d = (select mostCommon(sum(d), any_value(mid.value), 1) from cc, mid)
  ),
  ee as (
    select * from dd
    where e = (select mostCommon(sum(e), any_value(mid.value), 1) from dd, mid)
  )

  select * from cc
)


select * from oxygen```
```sql
DECLARE target_word STRING DEFAULT 'methinks';
DECLARE corpus_count, word_count INT64;

SET (corpus_count, word_count) = (
  SELECT AS STRUCT COUNT(DISTINCT corpus), SUM(word_count)
  FROM `bigquery-public-data`.samples.shakespeare
  WHERE LOWER(word) = target_word
);

SELECT
  FORMAT('Found %d occurrences of "%s" across %d Shakespeare works',
         word_count, target_word, corpus_count) AS result;```
```sql
DECLARE y int64;

with x as (
  EXECUTE IMMEDIATE "SELECT ? * (? + 2)" INTO y USING 1, 3;
)

select * from x
```
