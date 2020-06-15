<a name="pgtop"></a>

# Simple Banking System - JetBrains Academy Project

<a name="0"></a><a name="stages"></a>

source files folder path: [./ task / src / banking /](./task/src/banking/)

## Project Stages / Other Sections

1. [Card anatomy](#stage1)
2. [Luhn algorithm](#stage2)
3. [I'm so lite](#stage3)
4. [Advanced system](#stage4)
5. [Remarks](#remarks)

---

<a name="1"></a><a name="stage1"></a>

## Stage #1 - Card anatomy

Stage description [on JetBrains Academy site](https://hyperskill.org/projects/93/stages/515/implement) or [locally](./Card%20anatomy/task.html)

Stage start date: 2020-06-06

### Stage goals

* be familiar with bank card numbering system<br>CS/IT concepts used: 
  * requirements analysis, picking up relevant information from the description received,
  * choosing the right data type for the modeled slice of reality - eg.: card number: integer vs real vs string,
* generate unique bank card numbers<br>CS/IT concepts used: 
  * random number generator,
  * the _set_ data type, 
  * extracting logical parts from the string representing full card number (issuer ID,  user ID, check digit),
  * composing string representing the full card number from individual  parts,
* keep records about issued card's data: number, pin, balance<br>CS/IT concepts used: 
  * the _map_ data type,
* wait for user input, process the option he/she chooses<br>CS/IT concepts used:
  * main application  loop
  * finite automata basics - state of the application

<sub>[Top of page](#pgtop)<sub>

---

<a name="2"></a><a name="stage2"></a>

## Stage #2 - Luhn algorithm

Stage description [on JetBrains Academy site](https://hyperskill.org/projects/93/stages/516/implement) or [locally](./Luhn%20algorithm/task.html)

Stage start date: 2020-06-07

### Stage goals

* to be familiar with 'Luhn formula' - card number checking algorithm
* check digit generation
* checking the correctness of the given card number according to the Luhn formula and the check digit provided

CS/IT concepts used (common for all above-mentioned topics):

* Java 8 streams - processing IntStream based on string representing full card number - series of digits 
* generating / checking the remainder of division as a check digit

<sub>[Top of page](#pgtop)<sub>

---

<a name="3"></a><a name="stage3"></a>

## Stage #3 - I'm so lite

Stage description [on JetBrains Academy site](https://hyperskill.org/projects/93/stages/517/implement) or [locally](./I'm%20so%20lite/task.html)

Stage start date: 2020-06-09

### Stage goals

* ensuring the persistence of accumulated data

CS/IT concepts to use:

* CLI parameters utilization - eg. providing SQLite DB file name ,
* utilizing 'singleton' design  pattern - for new 'AccDataBase' class and for the 'Log' class refactored from single variable,
* working with SQLite DB,
* basic concepts of JPA (Java Persistent API): data source, connection, SQL statement,  prepared SQL statement, etc.,
* basic of DDL (Data Definition Language): create DB / create table, drop db, drop table,
* some of basic SQL CRUD operations: 
  * select ... from ... where ... , 
  * insert into ... values ..., 

<sub>[Top of page](#pgtop)<sub>

---

<a name="4"></a><a name="stage4"></a>

## Stage #4 - Advanced system

Stage description [on JetBrains Academy site](https://hyperskill.org/projects/93/stages/518/implement) or [locally](./Advanced%20system/task.html)

Stage start date: 2020-06-15

### Stage goals

* add more bank account-related operations: 
  * add income,
  * do transfer (to another account),
  * close account,

CS/IT concepts to use:

* update ... set ...,
* delete from ... where ...,

<sub>[Top of page](#pgtop)<sub>

---

<a name="5"></a><a name="remarks"></a>

## Remarks

This project has been included in the version control system (git) and published on GitHub just after stage #2 completion



<sub>[Top of page](#pgtop)<sub>