host cls
conn system/root
SET LINESIZE 32000;
SET PAGESIZE 40000;
drop user practicaClase cascade;

PROMPT =======================================================================
create user practicaClase identified by practicaClase; 
--Este usuario administra la base de datos.
grant dba to practicaClase;
conn practicaClase/practicaClase

drop table usuario;
create table usuario(
id number(3),
username varchar(10) not null,
password varchar(10) not null,
rol varchar(10) not null
);
drop table estudiante;
create table estudiante(
id number (3) ,
cedula varchar2(10) not null,
nombre   varchar2(50) not null,
apellidos   varchar2(50) not null,
edad  number(3)  not null,
userr number(3) not null
);

drop table cursosEstudiante;
create table cursosEstudiante(
id number (3) ,
id_estudiante number (3) not null,
id_curso number (3) not null
);

drop table curso;
create table curso(
id number (3) ,
descripcion varchar2 (50) not null,
nombre   varchar2(50) not null,
creditos  number(2)  not null
);


PROMPT ===============================================================================================================================
alter table usuario add constraint practicaClase_user_PK primary key (id);
alter table estudiante add constraint practicaClase_est_PK primary key (id);
alter table cursosEstudiante add constraint practicaClase_cE_PK primary key (id);
alter table curso add constraint practicaClase_curso_PK primary key (id);

alter table estudiante add constraint lab01_curso_UK unique (cedula);

alter table estudiante add constraint  estudiante_fk_user foreign key(userr) references usuario;
alter table cursosEstudiante add constraint cursosEstudiante_fk_estudiante foreign key (id_estudiante) references estudiante;
alter table cursosEstudiante add constraint cursosEstudiante_fk_curso foreign key (id_curso)  references curso;
PROMPT ===============================================================================================================================

PROMPT ===============================================================================================================================
create sequence sec_usuario   start with 1    increment by 1;
create sequence sec_curso   start with 1    increment by 1;
create sequence sec_estudiante  start with 1    increment by 1;
create sequence sec_cursosEstudiante   start with 1    increment by 1;
PROMPT ===============================================================================================================================

PROMPT ===============================================================================================================================
create or replace trigger ac_usuario BEFORE INSERT on usuario
REFERENCING OLD AS OLD NEW AS NEW
FOR EACH ROW
begin
  SELECT sec_usuario.NEXTVAL
  INTO   :new.id
  FROM   dual;
end ac_usuario;
/
create or replace trigger ac_estudiante BEFORE INSERT on estudiante
REFERENCING OLD AS OLD NEW AS NEW
FOR EACH ROW
begin
  SELECT sec_estudiante.NEXTVAL
  INTO   :new.id
  FROM   dual;
end ac_estudiante;
/

create or replace trigger ac_curso BEFORE INSERT on curso
REFERENCING OLD AS OLD NEW AS NEW
FOR EACH ROW
begin
  SELECT sec_curso.NEXTVAL
  INTO   :new.id
  FROM   dual;
end ac_curso;
/

create or replace trigger ac_cursosEstudiante BEFORE INSERT on cursosEstudiante
REFERENCING OLD AS OLD NEW AS NEW
FOR EACH ROW
begin
  SELECT sec_cursosEstudiante.NEXTVAL
  INTO   :new.id
  FROM   dual;
end ac_cursosEstudiante;
/

create or replace trigger delete_est_trg BEFORE DELETE OR UPDATE on estudiante
REFERENCING OLD AS OLD NEW AS NEW
FOR EACH ROW
begin
	delete from practicaClase.cursosEstudiante ce where ce.id_estudiante = :old.id;
	
end delete_est_trg;
/
show error
PROMPT ===============================================================================================================================

PROMPT ===============================================================================================================================
CREATE OR REPLACE PACKAGE types
AS
     TYPE ref_cursor IS REF CURSOR;
	 
END;
/

create or replace function PA_cursosDeEst(Pid in estudiante.id%type) return Types.ref_cursor as curso_cursor types.ref_cursor;
begin 
	open curso_cursor for 
		select c.id,c.descripcion,c.nombre,c.creditos from curso c,cursosEstudiante ce where ce.id_estudiante = Pid and c.id = ce.id_curso;
		-- select * from curso c INNER JOIN cursosEstudiante ce ON ce.id_estudiante = Pid;
return curso_cursor;
end;
/
show error
create or replace function PA_cursosList return Types.ref_cursor as curso_cursor types.ref_cursor;
begin 
	open curso_cursor for 
		-- select c.id,c.descripcion,c.nombre,c.creditos from curso c,cursosEstudiante ce where ce.cursosEstudiante = id and c.id = ce.id_curso;
		select * from curso c;
return curso_cursor;
end;
/


CREATE OR REPLACE PACKAGE p IS
    TYPE arrCursos IS TABLE OF NUMBER(3) INDEX BY BINARY_INTEGER;
  
    
  END p;
  /



create or replace procedure PA_eliminarCursosDeEst( est in estudiante.id%type) as 
begin 
	delete  from cursosEstudiante where id_estudiante = est;
end;
/

create or replace procedure PA_actualizarEst( Pid in estudiante.id%type,Pced in estudiante.cedula%type, Pnom in estudiante.nombre%type,
Papellidos in estudiante.apellidos%type, Pedad estudiante.edad%type, 	t_in in array_table) as 
begin 
	update estudiante set nombre = Pnom, cedula = Pced, apellidos = Papellidos, edad = Pedad where id = Pid;
	FOR i IN 1..t_in.count LOOP
		insert into cursosEstudiante(id_estudiante,id_curso) values (idInserted,t_in(i));
		
    END LOOP;
end;
/
PROMPT ===============================================================================================================================
create or replace function PA_listarEstudiantes
return Types.ref_cursor
as 
	estudiante_cursor types.ref_cursor;
begin
	open estudiante_cursor for 
		select * from estudiante;
return estudiante_cursor;
end;
/


CREATE TYPE array_table AS TABLE OF NUMBER;
/
create or replace procedure PA_insertarEstudiante(new_cedula in estudiante.cedula%type,
												new_nombre in estudiante.nombre%type,
												new_apellidos in estudiante.apellidos%type,
												new_edad in estudiante.edad%type,
												t_in in array_table,
												new_username in usuario.username%type,
												new_password in usuario.password%type,
												new_rol in usuario.rol%type
												)
as
idInserted number(3), idUsuario number(3);
begin 
	insert into usuario(username, password, rol) values(new_username, new_password, new_rol) returning id into idUsuario;
	insert into  estudiante(cedula, nombre,apellidos, edad, userr) 
	values(new_cedula, new_nombre,new_apellidos, new_edad, idUsuario) returning id into idInserted;
	
	FOR i IN 1..t_in.count LOOP
		insert into cursosEstudiante(id_estudiante,id_curso) values (idInserted,t_in(i));
		
    END LOOP;
end;
/
create or replace procedure PA_eliminarEstudiante(id in estudiante.id%type)
as
begin
	delete from estudiante where estudiante.id = id;
end;
/
create or replace function PA_getUsuario(new_username in usuario.username%type, new_password in usuario.password%type)
return Types.ref_cursor
as 
	usuario_cursor types.ref_cursor;
begin
	open usuario_cursor for 
		select * from usuario where username = new_username and password = new_password;
return usuario_cursor;
end;
/
insert into curso (descripcion,nombre,creditos) values ('123','c1',3);
insert into curso (descripcion,nombre,creditos) values ('123','c2',3);
insert into curso (descripcion,nombre,creditos) values ('123','c3',3);
insert into curso (descripcion,nombre,creditos) values ('123','c4',3);

insert into estudiante(cedula, nombre, apellidos, edad) values(111,'test','test',15);
select * from curso;

insert into usuario(username, password, rol) values('admin','admin','admin');
select * from estudiante;
select * from cursosEstudiante;
select PA_cursosDeEst(1) cursosDeEst from dual;
select PA_cursosList cursos from dual;
-- exec PA_eliminarCursosDeEst(1);
-- delete from estudiante ce where ce.id = 1;
-- select * from cursosEstudiante;
