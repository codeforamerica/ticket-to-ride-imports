# Aspen

[Aspen](http://www.follettlearning.com/webapp/wcs/stores/servlet/en/fssmarketingstore/student-and-instructional-management?intv_id=0&catalogId=10551&mpe_id=14252&langId=-1&evtype=CpgnClick&storeId=10154&ddkey=http:ClickInfo) is a student information system created by [Follett](http://www.follett.com/). It supports two kinds of imports for CSV files.

## Import Job Types

### Single Table Imports

This kind of import allows for data to be loaded directly from a CSV file into a single table. The import job is written in XML that maps CSV columns to table columns in Aspen's database. It also requires an input XML which defines arguments for the import job (such as the file to import, etc.)

### Multi Table Imports

Multi-table imports also import CSV files to be imported, but allow fields to be examined, transformed, or logic checks to be applied before fields get into the database. Additionally, as the name suggests, data can be inserted into multiple tables. Like single table imports, an input XML is required. However, the import job is written in Java and makes use of Follett and X2 Dev Corporation libraries (sadly, these libraries and their documentation are not available publicly).

## Procedures

Procedures are tasks that can be kicked off either manually or automatically. They can systematically pass arguments to an Import Job before running one. This is used for scheduling Import Jobs that run periodically (such as daily, hourly, etc.).

## Installing an Import Job and Scheduling It

(these instructions are a work in progress and will be documented later ... these are simply the expected steps)

1. Create new import job
1. Upload/paste source code for import job (XML or Java)
1. Upload/paste input XML for import job
1. Create new procedure
1. Configure time interval for which it'll run  