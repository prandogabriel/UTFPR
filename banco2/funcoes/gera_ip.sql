create function gera_IP() returns inet as
$$
begin
	return CONCAT(
	  TRUNC(RANDOM() * 250 + 2), '.' ,
	  TRUNC(RANDOM() * 250 + 2), '.',
	  TRUNC(RANDOM() * 250 + 2), '.',
	  TRUNC(RANDOM() * 250 + 2)
	)::INET;
end
$$ language plpgsql
