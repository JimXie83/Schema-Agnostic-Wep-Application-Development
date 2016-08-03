CREATE TABLE Orders (
    id              int(11)          NOT NULL   comment 'Id' auto_increment, 
    order_no        VARCHAR(10)      NOT NULL   comment 'Order No.',
    order_date      DATE             NOT NULL   comment 'Order Date',
    shipping_date   DATE 		                    comment 'Shipping Date',
    order_status    VARCHAR(16)			            comment 'Status',
    courier         varchar(16)			            comment 'Courier',
    order_handler   varchar(40)      NOT NULL   comment 'Handler',
    description	    varchar(40)      NOT NULL   comment 'Description',
    valid           TINYINT(1)                  comment 'Valid',
    PRIMARY KEY (id)
) ENGINE=INNODB;

CREATE TABLE OrderDetails  
 (  
    id              int(11)         NOT NULL    comment 'Id' auto_increment, 
    Order_ID        int             NOT NULL    comment 'Order Id', 
    product_name    VARCHAR(50)     NOT NULL    comment 'Product Name',
    UnitPrice       decimal(12,2)   NOT NULL    comment 'Unit Price',  
    Quantity        int             NOT NULL    comment 'Quantity',  
    Discount        decimal(2,2)    NOT NULL    comment 'Discount' DEFAULT 0,
    FOREIGN KEY (Order_ID) REFERENCES Orders (id),
    PRIMARY KEY     (id)
) ENGINE=INNODB;


create TABLE contacts (
    contact_id             int(11)           unsigned not null auto_increment comment 'Id',
    last_name              varchar(30)       not null                         comment 'Last Name',
    first_name             varchar(25)                                        comment 'First Name',
    birthday               date                                               comment 'Birthdate',
    brithplace             varchar(60)                                        comment 'Place of Birth',
    country                varchar(40)                                        comment 'Country',
    state                  varchar(40)                                        comment 'State',
    city                   varchar(40)                                        comment 'City',
    street                 varchar(40)                                        comment 'Street',
    zip                    varchar(40)                                        comment 'Zip',
   /*order_id               int(11)          comment 'Order Id',
    FOREIGN KEY (order_id) REFERENCES Orders (id),*/
    PRIMARY KEY (contact_id)
)ENGINE=INNODB;

  /******************Employees & Child tables*****************/

CREATE TABLE employees (
	emp_id      int(11)			NOT NULL comment 'Id' auto_increment, 
    emp_no      VARCHAR(6)      NOT NULL comment 'Employee No.',
    birth_date  DATE            NOT NULL comment 'Date of Birth',
    first_name  VARCHAR(14)				 comment 'First Name',
    last_name   VARCHAR(16)				 comment 'Last Name',
    gender      varchar(6)				 comment 'Gender',    
    hire_date   DATE            NOT NULL comment 'Hire Date',
    active      TINYINT(1)              comment 'Active',
    PRIMARY KEY (emp_id)
) ENGINE=INNODB;
CREATE TABLE titles (
	id          int(11)			NOT NULL        comment 'Id' auto_increment,
    emp_id      int(11)         NOT NULL  comment 'Emp Id',
    title       VARCHAR(50)     NOT NULL  comment 'Title',
    from_date   DATE					            comment 'From Date',
    to_date     DATE                      comment 'To Date',,
    FOREIGN KEY (emp_id) REFERENCES employees (emp_id),
    PRIMARY KEY (id)
) ENGINE=INNODB;
CREATE TABLE salaries (
	id          int(11)         NOT NULL comment 'Id' auto_increment,
    emp_id      int(11)         NOT NULL comment 'Emp Id',
    salary      INT                      comment 'Salary',
    from_date   DATE                     comment 'From Date',
    to_date     DATE					 comment 'To Date',
    FOREIGN KEY (emp_id) REFERENCES employees (emp_id),
    PRIMARY KEY (id)
) ENGINE=INNODB;

/****************Category*******************/
create TABLE Category (
    id                  int(11)           unsigned not null auto_increment comment 'Id',
    CategoryNo          varchar(40)       not null                         comment 'Category No.',
    CategoryName        varchar(40)       not null                         comment 'Category Name',
    Note                varchar(50)                                        comment 'Note',
    DateCreated         Date                                               comment 'Date Created',
    IsActive            TINYINT(1)                                         comment 'Active',
    PRIMARY KEY (id)
)ENGINE=INNODB;

/*******************************************/

CREATE TABLE Student
(
    id              int(11)          NOT NULL   comment 'Id' auto_increment,
    stud_no         VARCHAR(10)      NOT NULL   comment 'Student No.',
    first_name      varchar(40)			            comment 'First Name',
    last_name       varchar(40)      NOT NULL   comment 'Last Name',
    mid_name        varchar(40)                 comment 'Middle Name',
    enroll_date     DATE             NOT NULL   comment 'Enrolled On',
    birthdate       DATE 		                    comment 'Birthdate',
    GPA             decimal(4,2)			          comment 'GPA',
    Street          varchar(60)                 comment 'Street',
    City            varchar(60)                 comment 'City',
    State           varchar(60)                 comment 'State',
    Country         varchar(60)                 comment 'Country',
    Zip             varchar(8)                  comment 'Zip Code',
    Email           varchar(40)                 comment 'Email',
    valid           TINYINT(1)                  comment 'Valid',
    PRIMARY KEY (id)
) ENGINE=INNODB;

CREATE TABLE Courses
(
    id              int(11)         NOT NULL    comment 'Id' auto_increment,
    stud_id         int(11)         NOT NULL    comment 'Student Id',
    course_no       varchar(20)     NOT NULL    comment 'Course No',
    course_name     VARCHAR(50)                 comment 'Course Name',
    credit          int(2)                      comment 'Credit',
    start_date      DATE 		                    comment 'Start Date',
    FOREIGN KEY (stud_id) REFERENCES Student (id),
    PRIMARY KEY     (id)
) ENGINE=INNODB;
