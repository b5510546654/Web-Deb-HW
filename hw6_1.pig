inputs = LOAD './web-Google.txt' USING PigStorage('\t') AS (nodeA: int,nodeB: int);
groups = GROUP inputs BY nodeB;
counts = foreach groups generate group,COUNT(inputs);
sort = ORDER groups BY counts DESC;
store counts into './6-1result'; 

