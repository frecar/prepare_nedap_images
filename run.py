import pyodbc


sql_ip = "10.0.6.178"
sql_db = "aeosdb"
sql_user = "sa"
sql_pass = "Grolle@nedap1"


#cnxn = pyodbc.connect('DRIVER={SQL Server};SERVER=%s;DATABASE=%s;UID=%s;PWD=%s' % (sql_ip, sql_db, sql_user, sql_pass))
#cnxn.autocommit = True
#cur = cnxn.cursor()

#cur.execute('SELECT * FROM employee')


cnxn = pyodbc.connect('DRIVER={SQL Server};SERVER=10.0.6.178;DATABASE=aeosdb;UID=sa;PWD=Grolle@nedap1')
cursor = cnxn.cursor()