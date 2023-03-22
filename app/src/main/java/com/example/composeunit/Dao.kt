package com.example.composeunit
import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * User: YourPc
 * Date: 7/20/2017
 */
@Dao
interface ChinookDao {

    @get:Query("SELECT * FROM customers")
    val customers: List<Customers>

    @get:Query("SELECT * FROM employees")
    val employees: List<Employees>

    @get:Query("SELECT * FROM user")
    val users: Flow<List<User>>

    @get:Query("SELECT * FROM compose_data")
    val composeData:Flow<List<ComposeData>>
    @Insert
    suspend fun insertComposeData(composeData:ComposeData)
    //关联多表查询-写给郭上兰
//    @Transaction
//    @Query("SELECT * FROM customers INNER JOIN compose_data ON customers.CustomerId = compose_data.id  WHERE customers.CustomerId =:id LIMIT :limit OFFSET :offset")
//    fun paperList(id:Int,  limit:Int,  offset:Int):List<Customers>

}

@Entity(tableName = "compose_data")
data class ComposeData(
    @PrimaryKey @ColumnInfo(name = "id") var id: Int?=0,
    @ColumnInfo(name = "item_title") var item_title: String? = "",
    @ColumnInfo(name = "item_subtitle") var item_subtitle: String? = "",
    @ColumnInfo(name = "item_content") var item_content: String? = "",
    @ColumnInfo(name = "item_star") var item_star: Int?=0,
) {
    override fun toString(): String {
        return "ComposeData(id=$id, item_title=$item_title, item_subtitle=$item_subtitle, item_content=$item_content, item_star=$item_star)"
    }
}
@Entity(tableName = "user")
data class User(
    @PrimaryKey @ColumnInfo(name = "StudentId") var StudentId: Int = 0,
    @ColumnInfo(name = "Name") var name: String? = "",
    @ColumnInfo(name = "Age") var age: String? = "",
    @ColumnInfo(name = "Address") var address: String? = "",
    @ColumnInfo(name = "Country") var country: Int = 0,
){
    override fun toString(): String {
        return "User(StudentId=$StudentId, age='$age', address='$address', country=$country)"
    }
}

@Entity(tableName = "employees",
        foreignKeys = [ForeignKey(entity = Employees::class,
                parentColumns = ["EmployeeId"],
                childColumns = ["ReportsTo"])])
data class Employees(
    @PrimaryKey @ColumnInfo(name = "EmployeeId") var employeeId: Int = 0,
    @ColumnInfo(name = "LastName") var lastName: String = "",
    @ColumnInfo(name = "FirstName") var firstName: String = "",
    @ColumnInfo(name = "Title") var title: String? = "",
    @ColumnInfo(name = "ReportsTo", index = true) var reportsTo: String? = "",
    @ColumnInfo(name = "BirthDate") var birthDate: String? = "",
    @ColumnInfo(name = "HireDate") var hireDate: String? = "",
    @ColumnInfo(name = "Address") var address: String? = "",
    @ColumnInfo(name = "City") var city: String? = "",
    @ColumnInfo(name = "State") var state: String? = "",
    @ColumnInfo(name = "Country") var country: String? = "",
    @ColumnInfo(name = "PostalCode") var postalCode: String? = "",
    @ColumnInfo(name = "Phone") var phone: String? = "",
    @ColumnInfo(name = "Fax") var fax: String? = "",
    @ColumnInfo(name = "Email") var email: String? = ""
)


@Entity(tableName = "customers",
        foreignKeys = [ForeignKey(entity = Employees::class,
                parentColumns = ["EmployeeId"],
                childColumns = ["SupportRepId"])])
data class Customers(
        @PrimaryKey @ColumnInfo(name = "CustomerId") var id: Int = 0,
        @ColumnInfo(name = "FirstName") var FirstName: String = "",
        @ColumnInfo(name = "LastName") var LastName: String = "",
        @ColumnInfo(name = "Company") var Company: String? = "",
        @ColumnInfo(name = "Address") var Address: String? = "",
        @ColumnInfo(name = "City") var City: String? = "",
        @ColumnInfo(name = "State") var State: String? = "",
        @ColumnInfo(name = "Country") var Country: String? = "",
        @ColumnInfo(name = "PostalCode") var PostalCode: String? = "",
        @ColumnInfo(name = "Phone") var Phone: String? = "",
        @ColumnInfo(name = "Fax") var Fax: String? = "",
        @ColumnInfo(name = "Email") var Email: String = "",
        @ColumnInfo(name = "SupportRepId",index = true) var SupportRepId: Int? = 0
)