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

## Identifying Fields (or, Where data goes in the database)

1. Find a template that has the data you're looking for
1. Get the template name
1. Bring up the template in the template editor


## Get template name

If there's a screen in Aspen and you want to see where the information it collects goes into the database, you first need to know the name of the template. To get this, navigate to the screen and then pluck this information from the address bar.

![Template name in the address bar](aspen_template_id.png)

## Bring up the template in the template editor

1. Tools (top nav)
1. Templates (side nav)
1. CTRL+F (Win) or CMD+F (Mac)
1. Enter the template name
1. Click the name of the template
1. Design (side sub nav under Templates)

## See what template field maps to in database

In the template editor, hover your mouse over the desired field and click Edit. The pop-up window will display the Table and Field.


## Lookup a table or field in the data dictionary

1. Admin (top nav)
1. Data Dictionary or Tables (side left nav, the name changes depending on the current view)
1. (Click on the table you want)
1. Fields (side left nav)

