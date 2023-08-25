# SQL-Util
Aplicacao para auxiliar outros projetos com persistencia de dados. Para isso basta inserir nas dependencias dos projetos conforme exemplo abaixo:

```xml
<dependency>
    <groupId>diegosneves.sql</groupId>
    <artifactId>sql-util</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```
Lembrando de executar antes de mais nada nesse projeto mesmo o seguinte comando:

```shell
mvn clean install
```

Pois assim essa aplicacao será salva na pasta `.m2` e podendo ser encontrada nos demais projetos.

---
## Hibernate

Dentro da pasta `resources` deve ser criado um arquivo com o nome `hibernate.cfg.xml` conforme modelo abaixo:

```xml
<!DOCTYPE hibernate-configuration PUBLIC
    "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
    "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">
<hibernate-configuration>

    <session-factory>
        <property name="hibernate.dialect">org.hibernate.dialect.MySQL8Dialect</property>

        <!-- Configuração para usar o tipo de armazenamento InnoDB -->
        <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/bau?createDatabaseIfNotExist=true&amp;useUnicode=true&amp;characterEncoding=utf8</property>
        <property name="hibernate.connection.driver_class">com.mysql.cj.jdbc.Driver</property>
        <property name="hibernate.connection.username">root</property>
        <property name="hibernate.connection.password">Teste@123</property>
        <property name="hibernate.hbm2ddl.auto">update</property>

        <!-- Auto-detection das classes de entidade -->
        <mapping class="diegosneves.sql.model.Pessoa" /> <!-- classes que serao anotadas com @Entity -->
        <mapping class="diegosneves.sql.model.Veiculo" /> <!-- classes que serao anotadas com @Entity -->


    </session-factory>
</hibernate-configuration>

```
No exemplo acima as infos como:
- `database` = **bau**;
- `username` = **root**;
- `password` = **Teste@123**;

devem ser alteradas confome o projeto.

---
## Docker-Compose
Modelo de `docker-compose.yaml` para adicionar ao projeto.

```dockerfile
version: '3.9'

services:
  mongodb:
    image: mysql:latest
    restart: always
    container_name: sql_storage_space #Esse nome pode ser alterado para personalizar o projeto.
    networks:
      - compose-bridge
    ports:
      - "3306:3306"
    volumes:
      - db-sql-storage-space:/var/lib/mysql
    environment:
      - MYSQL_DATABASE=bau
      - MYSQL_ROOT_PASSWORD=Teste@123

volumes:
  db-sql-storage-space: #Esse nome pode ser alterado para personalizar o projeto.

networks:
  compose-bridge:
    driver: bridge
    
```
Esse arquivo deve ficar no mesmo nivel do `pom.xml`, ou seja, na raiz do projeto.

---
## SQL

Esses comandos abaixo foram necessarios para resolver um erro de conexao.

```mysql
SELECT User, Host, plugin FROM mysql.user WHERE User = 'root';
```

Ao usar o comando a acima por padrao o resultado será:

| User | Host      | plugin                |
|:-----|:----------|:----------------------|
| root | %         | caching_sha2_password |
| root | localhost | caching_sha2_password |

```mysql
  +------+-----------+-----------------------+
  | User | Host      | plugin                |
  +------+-----------+-----------------------+
  | root | %         | caching_sha2_password |
  | root | localhost | caching_sha2_password |
  +------+-----------+-----------------------+
```

Utilize os comandos abaixo para realizar as alteracoes necessarias.

```mysql
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'Teste@123';
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'Teste@123';
FLUSH PRIVILEGES;
```

Ao utilizar novamente esse comando `SELECT User, Host, plugin FROM mysql.user WHERE User = 'root';` o resultado esperado será:

| User | Host      | plugin                |
|:-----|:----------|:----------------------|
| root | %         | mysql_native_password |
| root | localhost | mysql_native_password |

```mysql
  +------+-----------+-----------------------+
  | User | Host      | plugin                |
  +------+-----------+-----------------------+
  | root | %         | mysql_native_password |
  | root | localhost | mysql_native_password |
  +------+-----------+-----------------------+
```


---