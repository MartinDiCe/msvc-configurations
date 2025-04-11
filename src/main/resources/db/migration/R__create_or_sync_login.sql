/* ======================================================================
   Crea el login de servidor si no existe
   ====================================================================== */
IF NOT EXISTS (SELECT 1 FROM sys.server_principals WHERE name = 'apiexport')
BEGIN
    PRINT 'Creating server login apiexport';
    CREATE LOGIN apiexport WITH PASSWORD = 'Osm3d1c4';
END
GO

/* ======================================================================
   A partir de aquí Flyway ya está en la BD master.
   Cambiamos a EGM, creamos el usuario y lo añadimos a db_owner
   ====================================================================== */
IF DB_ID('EGM') IS NOT NULL
BEGIN
    USE [EGM];
    IF NOT EXISTS (SELECT 1 FROM sys.database_principals WHERE name = 'apiexport')
BEGIN
        PRINT 'Creating database user apiexport in EGM';
        CREATE USER apiexport FOR LOGIN apiexport;
END

    -- Garantiza que tenga los permisos (puedes elegir otro rol si lo prefieres)
EXEC sp_addrolemember N'db_owner', N'apiexport';
END
GO
