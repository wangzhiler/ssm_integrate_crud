
import com.qaqa.crud.bean.Employee;
import com.qaqa.crud.dao.DepartmentMapper;
import com.qaqa.crud.dao.EmployeeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试DAO层的工作
 * 推荐Spring的项目就可以使用Spring的单元测试,可以自动注入我们需要的组件
 * 1. 先要导入SpringTest模块
 * 2. @ContextConfiguration指定Sring配置文件的位置
 * 3. 直接autowired要使用的组件即可
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MapperTest {

    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    //测试DepartmentMapper
    @Test
    public void testCRUD() {
        //1. 创建SpringIOC容器
//        ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
        //2. 从容器中获取mapper
//        DepartmentMapper bean = ioc.getBean(DepartmentMapper.class);

        System.out.println(departmentMapper);

        //1. 插入几个部门
//        departmentMapper.insertSelective(new Department(null, "销售部"));
//        departmentMapper.insertSelective(new Department(null, "测试部"));

        //2. 生成员工
        employeeMapper.insertSelective(new Employee(null, "coco", "F", "coco@123.com", 3));

        //3. 批量插入多个员工: 批量,使用可以执行批量操作的sqlSession
//        EmployeeMapper mapper = sqlSessionTemplate.getMapper(EmployeeMapper.class);
//        for(int i=0; i<1000; i++) {
//            String uid = UUID.randomUUID().toString().substring(0, 5) + i;
//            mapper.insertSelective(new Employee(null, uid, "M", uid + "@123.com", 1));
//        }
//        System.out.println("批量完成");
    }


}

