
## Zadatak:

Napraviti REST web-aplikaciju za računanje GDD-a (Growing Degree Days).

Formula računanja:

$$
\sum_{i=1}^{n} DD_{i}
$$
$$
DD_{i} = \frac{Tmax_{i}+Tmin_{i}}{2}-T_{min}
$$

Suma prolazi kroz sve dane.

$Tmax$ - maksimalna temperatura na kojoj biljka raste (30$\degree$C ili 86$\degree$F)
$Tmin$ - minimalna temperatura na kojoj biljka raste (10$\degree$C ili 50$\degree$F)
$Tmax_{i}$ - maksimalna dnevna temperatura
$Tmin_{i}$ - minimalna dnevna temperatura

### Ograničenja:
$$
Tmax_{i} =
\begin{cases}
Tmax &\text{if $Tmax_{i}>Tmax$} \\
Tmax_{i} &\text{else}
\end{cases}
$$

$$
DD_{i} =
\begin{cases}
0 &\text{if $DD_{i}<0$} \\
DD_{i} &\text{else}
\end{cases}
$$

Aplikaciji se šalje zahtjev, koji pročita podatke iz datoteke i računa DD za svaki dan. Ako je kumulativni izračun onda se zbrajaju vrijednosti prijašnjih dana (vrijednosti rastu).

### REST komunikacija

#### request
POST na `/gdd`
body:
```json
{
	"sensorId": "identifikator senzora",
	"plantingDate": "2022-05-15",
	"startDate": "2022-05-15",
	"endDate": "2022-07-20",
	"minTemp": 10,
	"maxTemp": 30,
	"cumulative": false
}
```

#### reponse

200 OK
```json
[
	{
		"date": "2022-05-16",
		"value": 10.4
	},
	{
		"date": "2022-05-17",
		"value": 15.2
	},
	...
	{
		"date": "2022-07-19",
		"value": 30.2
	},
]
```

### Datoteke

Nazivi datoteka:
- `sensorId_min.csv` - datoteka s minimumima po danima
- `sensotId_max.csv` - datoteka s maksimumima po danima

Primjer popisa datoteka:

```sh
0004A30B0021EF31_max.csv
0004A30B0021EF31_min.csv
0004A30B002204D0_max.csv
0004A30B002204D0_min.csv
0004A30B002220F4_max.csv
0004A30B002220F4_min.csv
```


Primjer sadržaja daoteke:
```csv
,result,table,_time,_value
,_result,0,2022-07-05T00:00:00Z,35.5
,_result,0,2022-07-06T00:00:00Z,20.900000000000006
,_result,0,2022-07-07T00:00:00Z,28.80000000000001
,_result,0,2022-07-08T00:00:00Z,22.900000000000006
,_result,0,2022-07-09T00:00:00Z,24.10000000000001
,_result,0,2022-07-11T00:00:00Z,24
,_result,0,2022-07-12T00:00:00Z,23
```


```sh
curl --request POST \
  http://localhost:8080/gdd/search  \
  --header 'Accept: application/json' \
  --header 'Content-type: application/json' \
  --data '{
	"sensorId": "0004A30B0021EF31",
	"startDate": "2022-10-01",
	"endDate": "2022-11-01",
	"minTemp": 10,
	"maxTemp": 30,
	"cumulative": false
}' | jq
```

