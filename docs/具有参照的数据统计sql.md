# demo
This is my first demo.


```
-- ================存储结果 ======================

-- ------------------  ml ----------------
DROP TABLE IF EXISTS `ml_goods_mx_result_091801`;


CREATE TABLE  mercadolibre.ml_goods_mx_result_091801  

SELECT  a3.id,a3.goods_id,a3.title ,a3.series, a3.actual_price
,a3.min_time, a3.min ,date_add(a3.min_time, interval (a3.total-1) day) as max_time ,a3.max
,a3.sub,a3.post_free,a3.os_warehouse    FROM  (
		 
				select a2.id,a2.goods_id,a2.title ,a2.series, a2.actual_price, count(1) as total ,a2.creat_time as min_time	,a2.post_free,a2.os_warehouse		 
			 ,MIN(a2.sales_volume+0) as min,MAX(a2.sales_volume+0) as max,(MAX(a2.sales_volume+0) - MIN(a2.sales_volume+0)) as sub 
			 from (
							select * from (
								SELECT id,goods_id,title,series,actual_price,sales_volume,date_format(creat_time ,'%Y-%m-%d' ) creat_time,IFNULL(post_free,0) post_free,IFNULL(os_warehouse,0) os_warehouse
								FROM ml_goods_mx
							) as temp
							group by goods_id,creat_time
				) a2 group by a2.goods_id,a2.title having count(1)>0   -- >1 过滤单条   > 0 包含单条
) as a3  
ORDER BY a3.series ASC,a3.sub DESC 


-- ------------------ BR ----------------

DROP TABLE IF EXISTS `ml_goods_br_result_091801`;


CREATE TABLE  mercadolibre.ml_goods_br_result_091801  

SELECT  a3.id,a3.goods_id,a3.title ,a3.series, a3.actual_price
,a3.min_time, a3.min ,date_add(a3.min_time, interval (a3.total-1) day) as max_time ,a3.max
,a3.sub ,a3.post_free,a3.os_warehouse    FROM  (
		 
				select a2.id,a2.goods_id,a2.title ,a2.series, a2.actual_price, count(1) as total ,a2.creat_time as min_time	,a2.post_free,a2.os_warehouse		 
			 ,MIN(a2.sales_volume+0) as min,MAX(a2.sales_volume+0) as max,(MAX(a2.sales_volume+0) - MIN(a2.sales_volume+0)) as sub 
			 from (
							select * from (
								SELECT id,goods_id,title,series,actual_price,sales_volume,date_format(creat_time ,'%Y-%m-%d' ) creat_time,IFNULL(post_free,0) post_free,IFNULL(os_warehouse,0) os_warehouse
								FROM ml_goods_br
							) as temp
							group by goods_id,creat_time
				) a2 group by a2.goods_id,a2.title having count(1)>0
) as a3  
ORDER BY a3.series ASC,a3.sub DESC 
```


```
-- 统计  MX  全量数据
SELECT  a3.id,a3.goods_id,a3.title ,a3.series, a3.actual_price
,a3.min_time, a3.min ,date_add(a3.min_time, interval (a3.total-1) day) as max_time ,a3.max
,a3.sub,types.entry    FROM  (
		 
				select a2.id,a2.goods_id,a2.title ,a2.series, a2.actual_price, count(*) as total ,a2.creat_time as min_time			 
			 ,MIN(a2.sales_volume+0) as min,MAX(a2.sales_volume+0) as max,(MAX(a2.sales_volume+0) - MIN(a2.sales_volume+0)) as sub 
			 from (
							select * from (
								SELECT id,goods_id,title,series,actual_price,sales_volume,date_format(creat_time ,'%Y-%m-%d' ) creat_time
								FROM ml_goods_mx
							) as temp
							group by goods_id,creat_time
				) a2 group by a2.goods_id,a2.title having count(1)>1
) as a3  
ORDER BY a3.series ASC,a3.sub DESC -- LIMIT 0,10000



-- 统计  MX  带类型
SELECT  a3.id,a3.goods_id,a3.title ,a3.series, a3.actual_price
,a3.min_time, a3.min ,date_add(a3.min_time, interval (a3.total-1) day) as max_time ,a3.max
,a3.sub,types.entry    FROM  (
		 
				select a2.id,a2.goods_id,a2.title ,a2.series, a2.actual_price, count(*) as total ,a2.creat_time as min_time			 
			 ,MIN(a2.sales_volume+0) as min,MAX(a2.sales_volume+0) as max,(MAX(a2.sales_volume+0) - MIN(a2.sales_volume+0)) as sub 
			 from (
							select * from (
								SELECT id,goods_id,title,series,actual_price,sales_volume,date_format(creat_time ,'%Y-%m-%d' ) creat_time
								FROM ml_goods_mx
							) as temp
							group by goods_id,creat_time
				) a2 group by a2.goods_id,a2.title having count(1)>1
) as a3  
LEFT  JOIN  
(SELECT a5.entry FROM
(
SELECT DISTINCT(entry)  FROM  ml_goods_type_mx WHERE series 
in ('Accesorios para Vehículos', 'Autos, Motos y Otros', 'Cámaras y Accesorios', 'Celulares y Telefonía' ,'Computación' , 'Electrodomésticos' ,
 'Hogar, Muebles y Jardín', 'Juegos y Juguetes')
) as a5  ) as types
ON a3.series = types.entry 
WHERE types.entry is NOT NULL
ORDER BY a3.series ASC,a3.sub DESC -- LIMIT 0,10000


-- BR
SELECT  a3.id,a3.goods_id,a3.title ,a3.series, a3.actual_price
,a3.min_time, a3.min ,date_add(a3.min_time, interval (a3.total-1) day) as max_time ,a3.max
,a3.sub,types.entry    FROM  (
		 
				select a2.id,a2.goods_id,a2.title ,a2.series, a2.actual_price, count(*) as total ,a2.creat_time as min_time			 
			 ,MIN(a2.sales_volume+0) as min,MAX(a2.sales_volume+0) as max,(MAX(a2.sales_volume+0) - MIN(a2.sales_volume+0)) as sub 
			 from (
							select * from (
								SELECT id,goods_id,title,series,actual_price,sales_volume,date_format(creat_time ,'%Y-%m-%d' ) creat_time
								FROM ml_goods_br
							) as temp
							group by goods_id,creat_time
				) a2 group by a2.goods_id,a2.title having count(1)>1
) as a3  
LEFT  JOIN  
(SELECT a5.entry FROM
(
SELECT DISTINCT(entry)  FROM  ml_goods_type_br WHERE series 
in ('Acessórios para Veículos', 'Brinquedos e Hobbies', 'Câmeras e Acessórios', 'Carros, Motos e Outros' ,'Casa, Móveis e Decoração' , 'Celulares e Telefones' ,
 'Eletrodomésticos', 'Informática')
) as a5  ) as types
ON a3.series = types.entry 
WHERE types.entry is NOT NULL
ORDER BY a3.series ASC,a3.sub DESC -- LIMIT 0,10000
```


```
CREATE TABLE  mercadolibre.ml_goods_br_result_091901  

SELECT  a3.goods_id,a3.title ,a3.series, a3.actual_price
,a3.min_time, a3.min ,date_add(a3.min_time, interval (a3.total-1) day) as max_time ,a3.max
,a3.sub,a3.post_free,a3.os_warehouse FROM  (
		 
				select a2.goods_id,a2.title ,a2.series, a2.actual_price, count(1) as total ,a2.creat_time as min_time			 
			 ,MIN(a2.sales_volume+0) as min,MAX(a2.sales_volume+0) as max,(MAX(a2.sales_volume+0) - MIN(a2.sales_volume+0)) as sub ,MAX(a2.post_free+0) as post_free,MAX(a2.os_warehouse+0) as os_warehouse 
			 from (
							select * from (
								SELECT `goods_id`, `title`, `series`, actual_price+'' as actual_price, max+'' as 'sales_volume' , date_format(max_time ,'%Y-%m-%d' ) creat_time, IFNULL(post_free,0) post_free,IFNULL(os_warehouse,0) os_warehouse
								FROM ml_goods_br_result_091802 
								UNION ALL
								SELECT goods_id,title,series,actual_price,sales_volume,date_format(creat_time ,'%Y-%m-%d' ) creat_time,IFNULL(post_free,0) post_free,IFNULL(os_warehouse,0) os_warehouse
								FROM ml_goods_br
							) as temp
							group by goods_id,creat_time
				) a2 group by a2.goods_id,a2.title having count(1)>0
) as a3  
ORDER BY a3.series ASC,a3.sub DESC -- LIMIT 0,10000

--  ------- 导出
SELECT COUNT(1) FROM ml_goods_br_result_091802
SELECT DISTINCT(goods_id) from ml_goods_br_result_091802

SELECT * FROM ml_goods_br_result_091802
GROUP BY goods_id having count(1)>1

SELECT * FROM ml_goods_br_result_091802 WHERE goods_id = 'MLB1002505011'


SELECT 
result.goods_id,result.title,result.series,result.actual_price,
date_format(result.min_time ,'%Y-%m-%d' ) min_time,  CONCAT(result.min,'') min,
date_format(result.max_time ,'%Y-%m-%d' ) max_time, CONCAT(result.max,'') max, CONCAT(result.sub,'') sub,types.entry 
,result.post_free,result.os_warehouse

FROM ml_goods_br_result_091901 as result
LEFT  JOIN  
(SELECT a5.entry FROM
(
SELECT DISTINCT(entry)  FROM  ml_goods_type WHERE area='BR' AND series 
in ('Acessórios para Veículos', 'Brinquedos e Hobbies', 'Câmeras e Acessórios', 'Carros, Motos e Outros' ,'Casa, Móveis e Decoração' , 'Celulares e Telefones' ,
 'Eletrodomésticos', 'Informática')
) as a5  ) as types
ON result.series = types.entry 
WHERE types.entry is NOT NULL
ORDER BY result.series ASC,result.sub DESC LIMIT 0,100



-- ================  MX ==========================
-- ml_goods_mx_result_091801
-- ml_goods_mx_result_091802
-- ml_goods_mx_result_091901
CREATE TABLE  mercadolibre.ml_goods_mx_result_091901  

SELECT  a3.goods_id,a3.title ,a3.series, a3.actual_price
,a3.min_time, a3.min ,date_add(a3.min_time, interval (a3.total-1) day) as max_time ,a3.max
,a3.sub,a3.post_free,a3.os_warehouse FROM  (
		 
				select a2.goods_id,a2.title ,a2.series, a2.actual_price, count(1) as total ,a2.creat_time as min_time			 
			 ,MIN(a2.sales_volume+0) as min,MAX(a2.sales_volume+0) as max,(MAX(a2.sales_volume+0) - MIN(a2.sales_volume+0)) as sub ,MAX(a2.post_free+0) as post_free,MAX(a2.os_warehouse+0) as os_warehouse 
			 from (
							select * from (
								SELECT `goods_id`, `title`, `series`, actual_price+'' as actual_price, max+'' as 'sales_volume' , date_format(max_time ,'%Y-%m-%d' ) creat_time ,IFNULL(post_free,0) post_free,IFNULL(os_warehouse,0) os_warehouse
								FROM ml_goods_mx_result_091802
								UNION ALL
								SELECT goods_id,title,series,actual_price,sales_volume,date_format(creat_time ,'%Y-%m-%d' ) creat_time ,IFNULL(post_free,0) post_free,IFNULL(os_warehouse,0) os_warehouse
								FROM ml_goods_mx
							) as temp
							group by goods_id,creat_time
				) a2 group by a2.goods_id,a2.title having count(1)>0
) as a3  
ORDER BY a3.series ASC,a3.sub DESC -- LIMIT 0,10000

-- 导出  164519
SELECT result.*,types.entry FROM ml_goods_mx_result_091901 as result
LEFT  JOIN  
(SELECT a5.entry FROM
(
SELECT DISTINCT(entry)  FROM  ml_goods_type_mx WHERE series 
in ('Accesorios para Vehículos', 'Autos, Motos y Otros', 'Cámaras y Accesorios', 'Celulares y Telefonía' ,'Computación' , 'Electrodomésticos' ,
 'Hogar, Muebles y Jardín', 'Juegos y Juguetes')
) as a5  ) as types
ON result.series = types.entry 
WHERE types.entry is NOT NULL
ORDER BY result.series ASC,result.sub DESC -- LIMIT 96000,100000

```
