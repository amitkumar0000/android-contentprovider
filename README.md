# android-contentprovider

ContentProvider   SQLiteOpenHelper

Basic Functionality
   Insert, Query , Delete functionality 

DB read is implemented in AsyncTask

AsyncTask is not the right choice to do Background processing in Activity Lifecycle.
On Screen Rotation, asynctask will leak memory.

AsyncTask Loader can be used in place of AsyncTask

CusorLoader is must better choice than AsyncTaskLoader as Cursor Loader will notify any underlying data change in DB
   
