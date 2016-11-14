SELECT type from ny_item WHERE id  IN(
SELECT ny_rank.objid from ny_rank
WHERE ny_rank.objtype=2
);

UPDATE ny_item set type=2 WHERE id  IN(
SELECT ny_rank.objid from ny_rank
WHERE ny_rank.objtype=2
);