CREATE TABLE DATA_TBL(
	`DATA_ID` SERIAL PRIMARY KEY,
	`DATA_TYPE` VARCHAR(10) NOT NULL,
	`DATA` VARCHAR(1000) NOT NULL,
	`ENABLED` BOOLEAN DEFAULT TRUE,
	`CREATED_BY` VARCHAR(20) NOT NULL,
	`CREATED_ON` DATETIME NOT NULL,
	`UPDATED_BY` VARCHAR(20) NOT NULL,
	`UPDATED_ON` DATETIME NOT NULL
);

CREATE TABLE ALL_DATATYPE_TABLE(
	identity IDENTITY PRIMARY KEY NOT NULL,
	tinyint TINYINT,
	smallint SMALLINT,
	mediumint MEDIUMINT,
	integer INTEGER,
	int INT,
	serial SERIAL,
	bigint BIGINT,
	bigserial BIGSERIAL,
	decimal DECIMAL(18, 2),
	numeric NUMERIC(18, 2),
	real REAL,
	double DOUBLE,
	float FLOAT,
	boolean BOOLEAN,
	date DATE,
	time TIME,
	datetime DATETIME,
	timestamp TIMESTAMP,
	char CHAR(16),
	varchar VARCHAR(255),
	character_varying CHARACTER VARYING(100),
	longvarchar LONGVARCHAR,
	text TEXT,
	binary BINARY(16),
	varbinary VARBINARY(16),
	longvarbinary LONGVARBINARY,
	blob BLOB,
	uuid UUID,
	"array" VARCHAR(1) ARRAY[1],
	enum ENUM('A','B','C','D'),
	geometry GEOMETRY,
	json JSON,
	clob CLOB,
	longtext LONGTEXT
);
