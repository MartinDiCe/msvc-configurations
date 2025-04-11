-- Ejecuta siempre; crea EGM solo si no existe
IF NOT EXISTS (SELECT 1 FROM sys.databases WHERE name = 'EGM')
BEGIN
  PRINT 'Creating database EGM';
  CREATE DATABASE [EGM];
END