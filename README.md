# android-contentprovider

ContentProvider   SQLiteOpenHelper

Basic Functionality
   Insert, Query , Delete functionality 

DB read is implemented in AsyncTask

AsyncTask is not the right choice to do Background processing in Activity Lifecycle.
On Screen Rotation, asynctask will leak memory.

AsyncTask Loader can be used in place of AsyncTask
   
