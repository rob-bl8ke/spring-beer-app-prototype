

# What the Script Does...

1. **Drops the `restdb` database if it exists:**  
   Ensures that if a database named `restdb` already exists, it is deleted before recreating it.  

   ```sql
   DROP DATABASE IF EXISTS restdb;
   ```

2. **Drops the `restadmin` user if it exists:**  
   Removes the user `restadmin` (if it exists) from the MySQL user list.  

   ```sql
   DROP USER IF EXISTS `restadmin`@`%`;
   ```

3. **Creates the `restdb` database if it doesn't exist:**  
   Creates a new database called `restdb` with UTF-8 character encoding and collation settings suited for supporting a wide range of characters (including emojis).  

   ```sql
   CREATE DATABASE IF NOT EXISTS restdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

4. **Creates the `restadmin` user if it doesn't exist:**  
   Creates a new user, `restadmin`, accessible from any host (`%`), with a password of `password`. The authentication plugin used is `mysql_native_password`.  

   ```sql
   CREATE USER IF NOT EXISTS `restadmin`@`%` IDENTIFIED WITH mysql_native_password BY 'password';
   ```

5. **Grants privileges to `restadmin` for the `restdb` database:**  
   Provides a wide range of privileges to the `restadmin` user on the `restdb` database, including basic CRUD operations (SELECT, INSERT, UPDATE, DELETE), schema modifications (CREATE, DROP, ALTER), and administrative functions (TRIGGER, EVENT).  

   ```sql
   GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, EXECUTE, CREATE VIEW, SHOW VIEW,
   CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON `restdb`.* TO `restadmin`@`%`;
   ```

### Key Intent:
The script prepares a fresh database (`restdb`) and sets up a user (`restadmin`) with comprehensive permissions to interact with and manage the database from any location.

# Issues with the password (from the tutorial)

The error you're encountering, **Error Code: 1524. Plugin 'mysql_native_password' is not loaded**, suggests that MySQL 8.4 is not recognizing the `mysql_native_password` authentication plugin. This issue typically arises because MySQL 8 uses `caching_sha2_password` as the default authentication plugin for new users, and `mysql_native_password` is not loaded by default.

### Solution:

1. **Use `caching_sha2_password` (recommended approach):**
   The default authentication method in MySQL 8.4 is `caching_sha2_password`, which is more secure than `mysql_native_password`. You can change the script to use this method instead of `mysql_native_password` by modifying the `CREATE USER` statement.

   Modify the line:

   ```sql
   CREATE USER IF NOT EXISTS `restadmin`@`%` IDENTIFIED WITH mysql_native_password BY 'password';
   ```

   To:

   ```sql
   CREATE USER IF NOT EXISTS `restadmin`@`%` IDENTIFIED WITH caching_sha2_password BY 'password';
   ```

   This will use the default `caching_sha2_password` plugin and should resolve the error.

2. **Alternatively, load the `mysql_native_password` plugin (if you must use it):**
   If you specifically need to use `mysql_native_password` for compatibility with older systems or tools, you can manually load the plugin and then use it. Here's how to do that:

   ```sql
   INSTALL PLUGIN mysql_native_password SONAME 'mysql_native_password.so';
   ```

   After running this command, try running your original script again.

3. **Check the current authentication plugin:**
   You can verify the current authentication plugin for a user by running the following:

   ```sql
   SELECT user, host, plugin FROM mysql.user;
   ```

   This will show which plugin is being used for each user in the MySQL instance.

### Summary:
- **Recommended:** Switch to `caching_sha2_password`, as it's the default in MySQL 8.4.
- **Alternative (if needed):** Manually load the `mysql_native_password` plugin.

