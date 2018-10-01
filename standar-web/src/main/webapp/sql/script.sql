CREATE OR REPLACE PROCEDURE BACK_UP_DB
(
JOB_MODE IN VARCHAR2,
EXP_NAME IN VARCHAR2,
SCHEMA_TABLE IN VARCHAR2
) 
IS
    handle       NUMBER;
BEGIN    
    
    handle := DBMS_DATAPUMP.open(
        operation   => 'EXPORT',
        job_mode    => JOB_MODE,
        remote_link => NULL,
        job_name    => EXP_NAME,
        version     => 'LATEST'
    );
    
    --Agregar el DataFile
    DBMS_DATAPUMP.add_file(handle => handle, filename  => EXP_NAME || '.dmp', directory => 'EXPORT_PLSQLP');
    
    --Agregar LOG
    DBMS_DATAPUMP.add_file(handle => handle, filename  => EXP_NAME || '.log', directory => 'EXPORT_PLSQLP', filetype  => DBMS_DATAPUMP.KU$_FILE_TYPE_LOG_FILE);
    
    IF JOB_MODE = 'SCHEMA'
    THEN
        -- Exportar esquema
        DBMS_DATAPUMP.metadata_filter(handle => handle, name   => 'SCHEMA_EXPR', value  => '=''' || SCHEMA_TABLE || '''');
    ELSE
        --Filter for the table
        DBMS_DATAPUMP.metadata_filter(handle => handle, name => 'NAME_LIST', VALUE => '''' || SCHEMA_TABLE || '''');
    END IF;
    
    DBMS_DATAPUMP.start_job(handle);
    
    DBMS_DATAPUMP.detach(handle);
END BACK_UP_DB;


EXECUTE BACK_UP_DB('TABLE', 'TABLE_EXPR', 'EMPLEADO');

--PARA EL MENEJO DE LA TABLA CONTROL

CREATE TABLE CONTROLEMPLEADO(
    ACCION VARCHAR2(50),
    USER_ACCION VARCHAR2(50),
    FECHA DATE,
    ESTADO_ACCION VARCHAR2(50)
);

CREATE OR REPLACE TRIGGER CONTROLEMPLEADOS
AFTER INSERT ON EMPLEADO
REFERENCING NEW AS NEW 
FOR EACH ROW 
BEGIN
    :NEW.LASTUSER;
    INSERT INTO CONTROLEMPLEADO (USUARIO, FECHA, DATONUEVO) VALUES(USER, SYSDATE, :NEW.DOCUMENTO);
END CONTROLEMPLEADOS;

