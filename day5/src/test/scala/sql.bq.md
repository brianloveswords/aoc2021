```sql
create temp function p(x int, y int)
returns struct<x int, y int>
as ( struct(x, y) );

create temp function mkline( 
    a struct<x int, y int>
  , b struct<x int, y int>
) returns struct< 
    a struct<x int, y int>
  , b struct<x int, y int>
  , slope int
> as (
  struct(
    a, b, 
    case when a.x - b.x = 0 
      then null
      else cast((a.y - b.y) / (a.x - b.x) as int64)
    end
  )
);

create temp function isDiagonal(
  line struct< 
      a struct<x int, y int>
    , b struct<x int, y int>
    , slope int>
) as ( line.slope is not null and line.slope != 0 );

create temp function lineContains( 
    line struct< 
        a struct<x int, y int>
      , b struct<x int, y int>
      , slope int>
  , c struct<x int, y int>
) as (true
  and c.x >= least(line.a.x, line.b.x) 
  and c.x <= greatest(line.a.x, line.b.x)
  and c.y >= least(line.a.y, line.b.y) 
  and c.y <= greatest(line.a.y, line.b.y)
  and case when line.slope is not null 
    then (c.y - line.a.y) = line.slope * (c.x - line.a.x)
    else true
  end
);

/*
  generate all the points between the start and end of a line (inclusive)

  we are dealing with a fixed grid, which simplifies things. we can "draw" a
  bounding box around the line then fill all points within the bounding box.
  we can then filter out all points that do not fall on the line using the
  `lineContains` function.
*/
create temp function lineCoverage(
  line struct< 
      a struct<x int, y int>
    , b struct<x int, y int>
    , slope int>
) returns array<struct<x int, y int>> as ((
  with base as (
    select p(x,y) as point
    from 
      unnest(generate_array(
        least(line.a.x, line.b.x),
        greatest(line.a.x, line.b.x)
      )) as x

      cross join

      unnest(generate_array(
        least(line.a.y, line.b.y),
        greatest(line.a.y, line.b.y)
      )) as y
  ) 
  select array_agg(point) 
  from base 
  where lineContains(line, point)
));

with lines as (
            select mkline(p(0,9), p(5,9)) as line
  union all select mkline(p(8,0), p(0,8))
  union all select mkline(p(9,4), p(3,4))
  union all select mkline(p(2,2), p(2,1))
  union all select mkline(p(7,0), p(7,4))
  union all select mkline(p(6,4), p(2,0))
  union all select mkline(p(0,9), p(2,9))
  union all select mkline(p(3,4), p(1,4))
  union all select mkline(p(0,0), p(8,8))
  union all select mkline(p(5,5), p(8,2))
)

, part1 as (
  select point.x, point.y, count(1) as danger
  from lines, unnest(lineCoverage(lines.line)) as point
  where not isDiagonal(lines.line)
  group by 1,2
  having danger > 1
)

, part2 as (
  select point.x, point.y, count(1) as danger
  from lines, unnest(lineCoverage(lines.line)) as point
  group by 1,2
  having danger > 1
)

select 'part1' as t, count(1) as c from part1
union all 
select 'part2' as t, count(1) as c from part2```
