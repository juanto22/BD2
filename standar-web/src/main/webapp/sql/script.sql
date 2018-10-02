CREATE OR REPLACE DIRECTORY EXPORT_PLSQLP AS 'C:\Users\juan.renderos\Desktop\EXPDP-PLSQL';

grant export full database to BDD2_LOCAL;

GRANT READ ON DIRECTORY EXPORT_PLSQLP TO PUBLIC; 

SELECT DIRECTORY_NAME, DIRECTORY_PATH FROM ALL_DIRECTORIES;

--Insertar
CREATE OR REPLACE PROCEDURE insertar_empleado
(
    e_cargo in EMPLEADO.CARGO%TYPE, 
    e_codigo IN EMPLEADO.CODIGO%type, 
    e_fecha_contratacion IN EMPLEADO.FECHACONTRATACION%type, 
    e_name IN EMPLEADO.NAME%TYPE, 
    e_salario IN EMPLEADO.SALARIO%TYPE,
    e_userID IN VARCHAR2
)
IS
BEGIN 
    Insert into EMPLEADO values (SEQ_EMPLEADO.nextval, e_cargo, e_codigo, e_fecha_contratacion, e_name, e_salario, e_userID);
    COMMIT WORK;
END insertar_empleado;

CREATE OR REPLACE PROCEDURE actualizar_empleado
(
    e_id IN EMPLEADO.ID%TYPE, 
    e_cargo in EMPLEADO.CARGO%TYPE, 
    e_codigo IN EMPLEADO.CODIGO%type, 
    e_fecha_contratacion IN EMPLEADO.FECHACONTRATACION%type, 
    e_name IN EMPLEADO.NAME%TYPE, 
    e_salario IN EMPLEADO.SALARIO%TYPE, 
    e_userID IN VARCHAR2
)    
IS
BEGIN 
    UPDATE EMPLEADO SET CARGO = e_cargo, CODIGO = e_codigo, FECHACONTRATACION = e_fecha_contratacion, NAME = e_name, SALARIO = e_salario, USER_ID = e_userID WHERE ID = e_id;
    COMMIT WORK;
END actualizar_empleado;

CREATE OR REPLACE PROCEDURE eliminar_empleado
(
    e_id IN EMPLEADO.ID%TYPE
) 
IS
BEGIN 
    delete EMPLEADO where ID = e_id;  
    DBMS_OUTPUT.put_line('¡Empleado Eliminado!');
    COMMIT WORK;
END eliminar_empleado;

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
    
    IF JOB_MODE = 'SCHEMA' THEN
        -- Exportar esquema
        DBMS_DATAPUMP.metadata_filter(handle => handle, name   => 'SCHEMA_EXPR', value  => '=''' || SCHEMA_TABLE || '''');
    ELSIF JOB_MODE = 'TABLE' THEN
        --Filter for the table
        DBMS_DATAPUMP.metadata_filter(handle => handle, name => 'NAME_LIST', VALUE => '''' || SCHEMA_TABLE || '''');
    END IF;
    
    DBMS_DATAPUMP.start_job(handle);
    
    DBMS_DATAPUMP.detach(handle);
END BACK_UP_DB;


call BACK_UP_DB('FULL', 'FULL_RESPALDO_TEST', 'EMPLEADO');

--PARA EL MENEJO DE LA TABLA CONTROL

CREATE TABLE CONTROLEMPLEADO(
    ACCION VARCHAR2(50),
    USER_ACCION VARCHAR2(50),
    FECHA DATE,
    ESTADO_ACCION VARCHAR2(50)
);

CREATE OR REPLACE TRIGGER CONTROLEMPLEADOS
BEFORE INSERT OR UPDATE OR DELETE ON EMPLEADO
FOR EACH ROW 
DECLARE
    user_type VARCHAR2(255);
    c_accion VARCHAR2(50);
    c_user VARCHAR2(255); 
BEGIN
    IF DELETING THEN
        c_accion := 'DELETING';
        select ROL.NAME INTO user_type from USERTYPEENTITY U INNER JOIN ROLETYPEENTITY ROL ON U.ROLID = ROL.ID where U.ID = :OLD.USER_ID;
        c_user := :OLD.USER_ID;
    ELSE
        select ROL.NAME INTO user_type from USERTYPEENTITY U INNER JOIN ROLETYPEENTITY ROL ON U.ROLID = ROL.ID where U.ID = :NEW.USER_ID;
        c_user := :NEW.USER_ID;
        IF INSERTING THEN
            c_accion := 'INSERTING';
        END IF;
        IF UPDATING THEN
            c_accion := 'UPDATING';
        END IF;
    END IF;
        
    IF user_type = 'guest' THEN
       
        insertar_control(c_accion, c_user, sysdate, 'Falla');
        RAISE_APPLICATION_ERROR(-20000, 'Usuario invitado no puede modifcar la tabla');
    ELSE
        insertar_control(c_accion, c_user, sysdate, 'Exito');
    END IF;
END CONTROLEMPLEADOS;

CREATE OR REPLACE PROCEDURE insertar_control
(
    c_accion in VARCHAR2, 
    c_user IN VARCHAR2, 
    c_fecha IN DATE, 
    e_estado IN VARCHAR2
)
IS PRAGMA AUTONOMOUS_TRANSACTION;
BEGIN 
    INSERT INTO CONTROLEMPLEADO VALUES(c_accion, c_user, c_fecha, e_estado);
    COMMIT WORK;
END insertar_control;

--PARA EXPORTAR UNA TABLA FINAL

CREATE OR REPLACE PROCEDURE EMP_TXT
(
TABLE_NAME IN VARCHAR2,
TXT_FILE_NAME IN VARCHAR2
) 
IS
    p_query varchar2(32767) := 'select * from ' || TABLE_NAME;
    l_theCursor     integer default dbms_sql.open_cursor;
    l_columnValue   varchar2(4000);
    l_status        integer;
    l_descTbl       dbms_sql.desc_tab;
    l_colCnt        number;
    n number := 0;
    l_values        varchar2(32767);
    v_file  UTL_FILE.FILE_TYPE;
  procedure p(msg varchar2) is
    l varchar2(4000) := msg;
  begin
    while length(l) > 0 loop
      dbms_output.put_line(substr(l,1,80));
      l := substr(l,81);
    end loop;
  end;
BEGIN  
    v_file := UTL_FILE.FOPEN(location => 'EXPORT_PLSQLP', filename => TXT_FILE_NAME || '.txt', open_mode => 'w', max_linesize => 32767);
  
    dbms_sql.parse(  l_theCursor,  p_query, dbms_sql.native );
    dbms_sql.describe_columns( l_theCursor, l_colCnt, l_descTbl );

    for i in 1 .. l_colCnt loop
        dbms_sql.define_column(l_theCursor, i, l_columnValue, 4000);
    end loop;

    l_status := dbms_sql.execute(l_theCursor);

    while ( dbms_sql.fetch_rows(l_theCursor) > 0 ) loop
        l_values := '';
        for i in 1 .. l_colCnt loop
            dbms_sql.column_value(l_theCursor, i, l_columnValue );
            if i = l_colCnt then
                l_values := l_values || l_columnValue;
            else
                l_values := l_values || l_columnValue || ',';
            end if;
        end loop;
        
        UTL_FILE.PUT_LINE(v_file, l_values);
        
        n := n + 1;
    end loop;
    if n = 0 then
      dbms_output.put_line( chr(10)||'No data found '||chr(10) );
    end if;
    
    UTL_FILE.FCLOSE(v_file);
  
    EXCEPTION
        WHEN OTHERS THEN
        UTL_FILE.FCLOSE(v_file);
    RAISE;
end EMP_TXT;

call EMP_TXT('EMPLEADO', 'MYFILENAME');


--PARA IMPORTAR la tabla empleado, final

CREATE OR REPLACE PROCEDURE IMP_TXT 
(
    p_fileDir  IN VARCHAR2,
    p_fileName IN VARCHAR2
) 
IS
    v_file UTL_FILE.FILE_TYPE;
    v_line  VARCHAR2(100); 
    sql_stmt VARCHAR2(255);
    c_values VARCHAR(255);
BEGIN
    v_file := UTL_FILE.FOPEN(p_FileDir, p_FileName, 'r');

    LOOP
        BEGIN
          UTL_FILE.GET_LINE(v_file, v_line);
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            EXIT;
        END;
    
        FOR i IN (SELECT trim(regexp_substr(v_line, '[^,]+', 1, LEVEL)) l FROM dual CONNECT BY LEVEL <= regexp_count(v_line, ',') + 1)
        LOOP
            IF regexp_like(i.l, '^\d+(\.\d+)?$') then
              c_values := c_values || i.l || ',';
            ELSE
              c_values :=  c_values || ''''|| i.l || ''',';
            end if;
            
        END LOOP;
        
        c_values := SUBSTR(c_values, 1, LENGTH(c_values) - 1);
        
        dbms_output.put_line(c_values);
        sql_stmt := 'INSERT INTO EMPLEADO VALUES ('|| c_values ||')';
        dbms_output.put_line('query: ' || sql_stmt);
        EXECUTE IMMEDIATE sql_stmt;
        
    END LOOP;

    UTL_FILE.FCLOSE(v_file);

    COMMIT;
    EXCEPTION
    WHEN OTHERS THEN
        UTL_FILE.FCLOSE(v_file);
    RAISE;
END IMP_TXT;
