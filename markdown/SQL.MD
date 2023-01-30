##### **DQL** 编写顺序

```sql
SELECT
FROM
WHERE
GROUP BY
HAVING
ORDER BY
LIMIT
```

##### **DQL** 执行顺序

```sql
FROM
WHERE
GROUP BY
HAVING
SELECT
ORDERY BY
LIMIT
```

---

##### 多表查询

- 一对一 (任意一方管理)
- 一对多 (多的一方关联)
- 多对多 (中间表关联)

---

##### 连接查询

- 内连接 (查询2者的交集)
  - 隐式内连接 SELECT A.* FROM **A, B** WHERE A.bid = B.id
  - 显示内连接 SELECT A.* FROM **A [INNER] JOIN B ON** A.bid = B.id
- 外连接 (查询自己和2者的交集)
  - 左外连接 (左表和交集) SELECT B.* FROM A **LEFT [OUTER] JOIN** B **ON** A.bid = B.id
  - 右外连接 (右表和交集) SELECT B.* FROM A **RIGHT [OUTER] JOIN** B **ON** A.bid = B.id
- 自连接 (自己连接自己查, 可以是内/外连接)
  - SELECT a1.name, a2.name FROM A a1 JOIN A a2 ON a1.mId = a2.id
- 联合查询 (多次查询结果结合形成一个新的结果, 查询列字段和字段类型必须相同)
  - **UNION** (去重)
  - **UNION ALL**
- 子查询 (又称嵌套查询, SQL中嵌套SELECT/UPDATE/INSERT/DELETE)
  - 标量子查询 (一行一列) OP: <、=、<=、>=
  - 列子查询 (一列N行) OP: IN、NOT IN、ANY、SOME、ALL
  - 行子查询 (一行N列) OP: IN、NOT IN、<>、=
  - 表子查询 (N行N列) OP: IN

---

##### SQL 示例

```sql
SELECT u.id, u.name,u.info, u.salary FROM `user` u;

SELECT
    u.name,
    u.salary,
    u.dept_id,
    u.salary,
    d.name,
    s.id,
    s.end_salary,
    s.start_salary
FROM
    `user` u,
    `dept` d,
    `salary` s
WHERE
    u.dept_id = d.id
    AND u.salary BETWEEN s.start_salary
    AND s.end_salary
ORDER BY u.salary DESC;

SELECT *, (
        SELECT COUNT(*)
        FROM `user`
        WHERE
            user.dept_id = dept.id
    ) C
FROM dept
ORDER BY C DESC;

SELECT d.create_time, u.id
FROM `dept` d, `user` u
WHERE (u.dept_id, u.id) in ( (d.id, 9), (d.id, 8));

SELECT u.id, u.name,u.info,salary,dept_id FROM `user` u;

BEGIN;

COMMIT;

ROLLBACK;

SET @N = '1';

SET @LINE = '|';

SELECT @N 'N', GROUP_CONCAT(
        id
        ORDER BY
            id SEPARATOR '_'
    ) ID,
    GROUP_CONCAT(
        name
        ORDER BY
            id SEPARATOR '_'
    ) NAME,
    GROUP_CONCAT(
        create_time
        ORDER BY
            id SEPARATOR '_'
    ) TIME
FROM dept;

SELECT CONCAT_WS('__', id, name) ID FROM dept;

SELECT
    GROUP_CONCAT(
        CONCAT_WS('', id, name) SEPARATOR '__'
    ) ID
FROM dept;

SELECT GROUP_CONCAT(name SEPARATOR '_') FROM dept;

SELECT CONVERT(id, CHAR) ID,create_time,name FROM dept;

SELECT CAST(id AS CHAR) FROM dept;

SELECT u.name, d.id, d.name
from `user` u
    JOIN dept d ON u.dept_id = d.id;

SELECT id, create_time,name FROM dept;

SET @C = 0;

SET @LEN = 0;

SET @VAR_LEN = 0;

SELECT
    name,
    @C := SUBSTR(info, 2, 5) C,
    @LEN := LENGTH(@C),
    @VAR_LEN := CHAR_LENGTH(@C)
FROM `user`
WHERE @LEN <> @VAR_LEN;

SELECT
    CONCAT_WS(
        '_',
        DATABASE(),
        VERSION(),
        USER(),
        INET_ATON('127.0.0.1'),
        INET_NTOA(2130706433)
    );

SELECT IFNULL(0, 99);

SELECT IFNULL(NULL, 99);

-- CONCAT 多列 -> 1列

-- GROUP_CONCAT 多行 -> 1行

SELECT GROUP_CONCAT(
        id
        ORDER BY
            id SEPARATOR '_'
    ) ID,
    GROUP_CONCAT(
        name
        ORDER BY
            id SEPARATOR '_'
    ) NAME
FROM dept;

SELECT id,create_time,name FROM dept;

-- 建表

CREATE TABLE
    `t_school` (
        `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
        `i18n_name` varchar(500) DEFAULT NULL COMMENT '国际化名称',
        PRIMARY KEY (`id`)
    ) ENGINE = InnoDB AUTO_INCREMENT = 3 DEFAULT CHARSET = utf8;

-- 添加数据

INSERT INTO
    `t_school`(`id`, `i18n_name`)
VALUES (
        1,
        '[{\"locale\":\"zh_CN\",\"name\":\"清华大学\"},{\"locale\":\"zh_TW\",\"name\":\"清華大學\"},{\"locale\":\"en_US\",\"name\":\"Tsinghua University\"},{\"locale\":\"th_TH\",\"name\":\"Qinghua มหาวิทยาลัย\"},{\"locale\":\"vi_VN\",\"name\":\"Tiểu Sinh\"}]'
    ), (
        2,
        '[{\"locale\":\"zh_CN\",\"name\":\"北京大学\"},{\"locale\":\"zh_TW\",\"name\":\"北京大學\"},{\"locale\":\"en_US\",\"name\":\"Peking University\"},{\"locale\":\"th_TH\",\"name\":\"มหาวิทยาลัยปักกิ่ง\"},{\"locale\":\"vi_VN\",\"name\":\"Đại học Bắc Kinh\"}]'
    );

ALTER TABLE
    locale_school CHANGE create_time create_time DATETIME DEFAULT NOW() COMMENT '创建时间';

SET @SSID = 20;

INSERT INTO
    locale_school (sid, sname, locale)
VALUES (@SSID, '清华大学', 'zh_CN'), (@SSID, '清華大學', 'zh_TW'), (
        @SSID,
        'Tsinghua University',
        'en_US'
    ), (
        @SSID,
        'Qinghua มหาวิทยาลัย',
        'th_TH'
    ), (@SSID, 'Tiểu Sinh', 'vi_VN');

SET @SSID = 30;

INSERT INTO
    locale_school(sid, sname, locale)
VALUES (@SSID, '北京大学', 'zh_CN'), (@SSID, '北京大學', 'zh_TW'), (
        @SSID,
        'Peking University',
        'en_US'
    ), (
        @SSID,
        'มหาวิทยาลัยปักกิ่ง',
        'th_TH'
    ), (
        @SSID,
        'Đại học Bắc Kinh',
        'vi_VN'
    );

SELECT id,s_id FROM t_school;

SELECT
    id,
    JSON_EXTRACT(i18n_name, '$[*].name') AS name,
    JSON_EXTRACT(i18n_name, '$[*].locale') locale
FROM t_school;

SELECT id,s_id FROM t_school;

SELECT id,sname,sid,locale,create_time FROM locale_school;

SELECT
    t.id,
    t.s_id,
    ls.sname,
    ls.locale
FROM t_school t
    JOIN locale_school ls ON t.s_id = ls.sid;

SELECT id, s_id, (
        SELECT
            GROUP_CONCAT(sname SEPARATOR '|')
        FROM locale_school ls
        WHERE
            ls.sid = t.s_id
    ) snames, (
        SELECT
            GROUP_CONCAT(locale SEPARATOR '|')
        FROM locale_school ls
        WHERE
            ls.sid = t.s_id
    ) locales
FROM t_school t
GROUP BY id
HAVING id > 0;

SELECT id,s_id FROM t_school;

-- 转义 ESCAPE

SELECT * FROM `user` WHERE info LIKE '%/%%' ESCAPE '/';

SELECT id,sname,sid,locale,create_time FROM locale_school;

SELECT
    id,
    name,
    info,
    salary,
    dept_id
FROM `user`
GROUP BY id
HAVING salary > 10000
ORDER BY salary DESC;

SELECT
    id,
    name,
    salary,
    CAST(salary AS DECIMAL(10, 2)),
    CONVERT(salary, DECIMAL(10, 3)),
REPLACE (FORMAT(salary + 1, 2), ',', '')
FROM `user`;

DESC SELECT @@have_profiling, @@slow_query_log, @@long_query_time;

SELECT @@local_infile;

SHOW profiles;

-- 精度 -> 数字的位数，标度 -> 小数点后的位数

DESC
SELECT
    id,
    create_time,
    degree,
    start_salary,
    end_salary
FROM salary;

-- 空间 -> 段 -> 区(1MB, 一共64个连续的页) -> 页(16KB) -> 行

SHOW CREATE TABLE dept;

SHOW variables LIKE '%hash_index%';

SHOW variables LIKE '%log_buffer_size%';

SHOW INDEX FROM user;

SHOW CREATE TABLE `order`;

SELECT id,create_time,degree,start_salary,end_salary FROM salary;

SELECT id,create_time,name FROM dept;

SELECT id,name,info,salary,dept_id FROM `user`;

SELECT id,store FROM test;

SELECT id,name FROM `test_order`;

SELECT
    id,
    name,
    info,
    salary,
    dept_id
FROM `user`
WHERE name LIKE '%/%' ESCAPE '/';

-- 列子查询, 一列多行

SELECT name FROM dept WHERE id IN (1, 2, 3);

SELECT salary, name FROM emp WHERE id = 1;

SELECT
    CAST(AVG(salary) AS DECIMAL(7, 1)),
    CAST(MAX(salary) AS DECIMAL(7, 1))
FROM emp;

SELECT *
FROM emp
WHERE salary BETWEEN (
        SELECT
            CAST(AVG(salary) AS DECIMAL(7, 1))
        FROM emp
    ) AND (
        SELECT
            CAST(MAX(salary) AS DECIMAL(7, 1))
        FROM emp
    );

SELECT e.name, d.name d_name
FROM emp e, dept d
WHERE e.dept_id = d.id;

-- group_concat 行合并

-- concat 列合并

SELECT name, (
        SELECT COUNT(dept_id)
        FROM emp
        WHERE
            dept.id = dept_id
    ) count, (
        SELECT
            GROUP_CONCAT(
                name
                ORDER BY id SEPARATOR '_'
            )
        FROM emp
        WHERE dept_id = dept.id
    ) names
FROM dept;

-- concat 一列多行
SELECT CONCAT_WS(';', id, name) FROM dept;

-- group_concat 一行多列
SELECT @names := GROUP_CONCAT(
        name
        ORDER BY
            id SEPARATOR '_'
    ) names,
    @ids := GROUP_CONCAT(
        id
        ORDER BY id SEPARATOR '_'
    ) ids, CONCAT(@names, ':', @ids) s
FROM dept;

SELECT
    COUNT(dept_id) `count`,
    dept_id, (
        SELECT name
        FROM dept
        WHERE id = dept_id
    ) name
FROM emp
GROUP BY dept_id;

SELECT sid, (
        SELECT GROUP_CONCAT(
                sname
                ORDER BY locale SEPARATOR ';'
            )
        FROM locale_school
        WHERE l.sid = sid
    ) snames, (
        SELECT GROUP_CONCAT(locale ORDER BY locale SEPARATOR ';') FROM locale_school WHERE l.sid = sid
    ) slocales
FROM locale_school l
GROUP BY sid;

SELECT name, dept_id, (SELECT name FROM dept WHERE id = dept_id) FROM emp;

SELECT COUNT(name), name FROM dept GROUP BY name;

SELECT name FROM dept ORDER BY field(name, 'C', 'V', 'L', 'E', 'G');

SELECT d.name, e.name FROM dept d JOIN emp e ON d.id = e.dept_id;

SELECT name, LENGTH(name), CHAR_LENGTH(name) FROM emp;

SELECT
    dept_id,
    COUNT(dept_id), (
        SELECT name
        FROM dept
        WHERE emp.dept_id = id
    )
FROM emp
GROUP BY dept_id;

SELECT
    e.name,
    e.dept_id,
    e.salary,
    d.name
FROM emp e, dept d
WHERE
    d.name IN ('C', 'V')
    and e.dept_id = d.id
ORDER BY d.id DESC;

SELECT
    e.name,
    e.salary,
    d.name
FROM emp e, dept d
WHERE e.dept_id = d.id;

SELECT
    d.id,
    d.name,
    MAX(e.salary) h_salary, (
        SELECT name
        FROM emp e1
        WHERE
            e1.salary = MAX(e.salary)
    ) h_name,
    MIN(e.salary) l_s, (
        SELECT name
        FROM emp e1
        WHERE
            e1.salary = MIN(e.salary)
    ) lname,
    CONVERT(
        CAST(AVG(e.salary) AS DECIMAL(8, 1)),
        CHAR
    ) avg_s
FROM dept d, emp e
WHERE d.id = e.dept_id
GROUP BY d.id
ORDER BY d.id;

SELECT name,
salary
FROM emp
WHERE salary > any (
        SELECT salary
        FROM emp
    )
ORDER BY salary;

SELECT name, salary
FROM emp
WHERE salary > (
        SELECT MIN(salary)
        FROM (
                SELECT salary
                FROM emp
            ) e
    )
ORDER BY salary;

SELECT e.name, e.salary, (
        SELECT name
        FROM dept
        WHERE id = e.dept_id
    )
FROM emp e;

SELECT * FROM emp;

SELECT * FROM emp WHERE (dept_id, salary) = (3, 10000.00);

SELECT * FROM emp WHERE (dept_id) = (3);

SELECT *
FROM emp
WHERE (dept_id) IN (
        SELECT id
        FROM dept
        WHERE name IN ('C', 'V')
    );

SELECT *
FROM emp
WHERE (dept_id, salary) = (
        SELECT dept_id, salary
        FROM emp
        WHERE (name) = ('lee')
    );

SELECT emp.*, d.name
FROM emp, dept d
WHERE (emp.dept_id) = (d.id)
    AND (dept_id) = (
        SELECT dept_id
        FROM emp
        WHERE (name) = ('lee')
    )
    AND (salary) > (
        SELECT salary
        FROM emp
        WHERE (name) = ('lee')
    );

SELECT *
FROM emp
WHERE (dept_id) IN (
        SELECT id
        FROM dept
        WHERE id < 3
    ) ORDER BY dept_id, salary DESC;

SELECT * FROM emp;

SELECT *
FROM emp
WHERE (dept_id, salary) IN (
        SELECT
            dept_id,
            salary
        FROM emp
        WHERE name = 'zilong'
    );

```

---
