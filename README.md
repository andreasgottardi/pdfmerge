# Configurable pdf merging utility

## Logging

The log destination can be configured by specifying the log directory. Default is a subfolder of the execution directory called "log".

```powershell
java -Dlogdir=C:\ProgramData\PdfMerge -jar goa.systems.pdfmerge.jar
```

## Actions
The following actions can be applied. They are executed in the order they are specified in the command line.
### Extract action
```
-a "e;filename.pdf;pagenumber"
```
### Fill action
```
-a "f;filename.pdf;key=value,name=prename,id=4711"
```
The comma charactor (,) is a separator of key value pairs. If a comma is required in either a key or a value it has to be escaped by backslash (\\,).

The equal character (=) is a separator of key and value. If a equal character is required in either a key or value it has to be escaped by backslash (\\=).

The semicolon character (;) is a separator of segments. If a semicolon character is required it has to be escaped by backslash (\\;).