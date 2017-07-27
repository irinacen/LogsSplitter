[LogSplitter v1.0]
------------------

DESCRIPTION:
------------
  
Tool that reads all the log files of a given directory and reorganizes it in new files separating it according to the thread that has written each trace and ordering the traces of each output file chronologically.

The entry pattern for the date in the traces, which will allow to order it chronologically, must be one of the following:
dd-MM-yyyy HH:mm:ss,SSS | dd-MM HH:mm:ss,SSS | dd-MM-yyyy HH: mm:ss | dd-MM HH: mm:ss

The name of each thread in the traces, which will be used to sort and create each output file, must have the following format in order to be processed: 
[ThreadName]

The input directory should contains only the log files to be processed (logs in subfolders are allowed).

The number of output files will be the same number of threads inside log files. Thus is, one output file per thread.

Example of trace in input file: 
24-07-2017 21:45:50 INFO [Thread9] Blah blah blah SomeText: '25' hello(0).  

USAGE:
------

LogsSplitter {inputFolder} {outputFolder}

Mandatory arguments:

inputFolder		- Folder containing input log files. All files in folder will be processed, so the folder should contain only the log files.
outputFolder	- Folder where the resulting log files will be saved.