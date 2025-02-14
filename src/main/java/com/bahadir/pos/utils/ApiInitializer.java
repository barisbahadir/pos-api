package com.bahadir.pos.utils;

import com.bahadir.pos.entity.BaseStatus;
import com.bahadir.pos.entity.category.Category;
import com.bahadir.pos.entity.company.Company;
import com.bahadir.pos.entity.organization.Organization;
import com.bahadir.pos.entity.permission.Permission;
import com.bahadir.pos.entity.permission.PermissionType;
import com.bahadir.pos.entity.role.Role;
import com.bahadir.pos.entity.user.User;
import com.bahadir.pos.entity.user.AuthRole;
import com.bahadir.pos.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class ApiInitializer implements CommandLineRunner {

    private final CompanyRepository companyRepository;
    private final OrganizationRepository organizationRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiInitializer(CompanyRepository companyRepository,
                          OrganizationRepository organizationRepository,
                          PermissionRepository permissionRepository,
                          RoleRepository roleRepository,
                          CategoryRepository categoryRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.companyRepository = companyRepository;
        this.organizationRepository = organizationRepository;
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        String companyName1 = "ANADOLUBANK AS";
        String companyName2 = "BARIS KIRTASIYE";

        // Şirketler
        if (companyRepository.count() == 0) {
            Company company1 = Company.builder().name(companyName1).status(BaseStatus.ENABLE).orderValue(1).build();
            Company company2 = Company.builder().name(companyName2).status(BaseStatus.ENABLE).orderValue(2).build();
            companyRepository.saveAll(List.of(company1, company2));
            System.out.println("Default companies created!");
        }

        String branchName1 = "East Turkey Branch";
        String branchName2 = "South Turkey Branch";

        // Organizasyonlar
        if (organizationRepository.count() == 0) {
            Company company1 = companyRepository.findByName(companyName1).orElse(null);
            Company company2 = companyRepository.findByName(companyName2).orElse(null);

            Organization org1 = Organization.builder().name(branchName1).status(BaseStatus.ENABLE).orderValue(1).company(company1).build();
            Organization org1_1 = Organization.builder().name("R&D Department").status(BaseStatus.DISABLE).orderValue(1).parent(org1).company(company1).build();
            Organization org1_2 = Organization.builder().name("Marketing Department").status(BaseStatus.ENABLE).orderValue(2).parent(org1).company(company1).build();
            Organization org1_3 = Organization.builder().name("Finance Department").status(BaseStatus.ENABLE).orderValue(3).parent(org1).company(company1).build();

            Organization org2 = Organization.builder().name(branchName2).status(BaseStatus.ENABLE).orderValue(2).company(company2).build();
            Organization org2_1 = Organization.builder().name("R&D Department").status(BaseStatus.DISABLE).orderValue(1).parent(org2).company(company2).build();
            Organization org2_2 = Organization.builder().name("Marketing Department").status(BaseStatus.ENABLE).orderValue(2).parent(org2).company(company2).build();
            Organization org2_3 = Organization.builder().name("Finance Department").status(BaseStatus.ENABLE).orderValue(3).parent(org2).company(company2).build();

            organizationRepository.saveAll(List.of(org1, org1_1, org1_2, org1_3, org2, org2_1, org2_2, org2_3));
            System.out.println("Default organizations created!");
        }

        // İzinler (Tamamlandı)
        if (permissionRepository.count() == 0) {
            // Dashboard İzinleri
            Permission dashboard = Permission.builder().name("Dashboard").label("sys.menu.dashboard").icon("ic-analysis").type(PermissionType.CATALOGUE).route("dashboard").orderValue(1).build();
            Permission welcome = Permission.builder().name("Welcome").label("sys.menu.welcome").type(PermissionType.MENU).route("welcome").component("/dashboard/welcome/index.tsx").orderValue(1).parent(dashboard).build();
            Permission analysis = Permission.builder().name("Analysis").label("sys.menu.analysis").type(PermissionType.MENU).route("analysis").component("/dashboard/analysis/index.tsx").orderValue(2).parent(dashboard).build();
            Permission workbench = Permission.builder().name("Workbench").label("sys.menu.workbench").type(PermissionType.MENU).route("workbench").component("/dashboard/workbench/index.tsx").orderValue(3).parent(dashboard).build();

            // Management İzinleri
            Permission management = Permission.builder().name("Management").label("sys.menu.management").icon("ic-management").type(PermissionType.CATALOGUE).route("management").orderValue(2).build();
            Permission userIndex = Permission.builder().name("User").label("sys.menu.user.index").type(PermissionType.CATALOGUE).route("user").orderValue(1).parent(management).build();
            Permission userProfile = Permission.builder().name("Profile").label("sys.menu.user.profile").type(PermissionType.MENU).route("profile").component("/management/user/profile/index.tsx").orderValue(1).parent(userIndex).build();
            Permission userAccount = Permission.builder().name("Account").label("sys.menu.user.account").type(PermissionType.MENU).route("account").component("/management/user/account/index.tsx").orderValue(2).parent(userIndex).build();
            Permission systemIndex = Permission.builder().name("System").label("sys.menu.system.index").type(PermissionType.CATALOGUE).route("system").orderValue(2).parent(management).build();
            Permission systemOrganization = Permission.builder().name("Organization").label("sys.menu.system.organization").type(PermissionType.MENU).route("organization").component("/management/system/organization/index.tsx").orderValue(1).parent(systemIndex).build();
            Permission systemPermission = Permission.builder().name("Permission").label("sys.menu.system.permission").type(PermissionType.MENU).route("permission").component("/management/system/permission/index.tsx").orderValue(2).parent(systemIndex).build();
            Permission systemRole = Permission.builder().name("Role").label("sys.menu.system.role").type(PermissionType.MENU).route("role").component("/management/system/role/index.tsx").orderValue(3).parent(systemIndex).build();
            Permission systemUser = Permission.builder().name("User").label("sys.menu.system.user").type(PermissionType.MENU).route("user").component("/management/system/user/index.tsx").orderValue(4).parent(systemIndex).build();
            Permission systemUserDetail = Permission.builder().name("User Detail").label("sys.menu.system.user_detail").type(PermissionType.MENU).route("user/:id").component("/management/system/user/detail.tsx").hide(true).orderValue(5).parent(systemIndex).build();

            // Diğer İzinler (COMPONENTS, FUNCTIONS, MENU_LEVEL, ERRORS, OTHERS) - Buraya ekleyin

            permissionRepository.saveAll(List.of(
                    dashboard, welcome, analysis, workbench,
                    management, userIndex, userProfile, userAccount, systemIndex,
                    systemOrganization, systemPermission, systemRole, systemUser, systemUserDetail
                    // Diğer izinleri buraya ekleyin
            ));
            System.out.println("Default permissions created!");
        }

        // Roller (Örnek: Admin ve Test)
        if (roleRepository.count() == 0) {
            Role adminRole = Role.builder()
                    .name(AuthRole.ADMIN.name())
                    .label(AuthRole.ADMIN.name())
                    .status(BaseStatus.ENABLE)
                    .orderValue(1)
                    .description("Super Admin")
                    .build();
            // Admin rolüne tüm izinleri ekleyin
            adminRole.setPermissions(new HashSet<>(permissionRepository.findAll()));
            roleRepository.save(adminRole);

            Role testRole = Role.builder()
                    .name(AuthRole.TEST.name())
                    .label(AuthRole.TEST.name())
                    .status(BaseStatus.ENABLE)
                    .orderValue(2)
                    .description("Test User")
                    .build();
            // Test rolüne belirli izinleri ekleyin (Örnek)
            List<Permission> testPermissions = permissionRepository.findByLabelIn(List.of("sys.menu.dashboard", "sys.menu.components", "sys.menu.functions")); // Örnek izin etiketleri
            testRole.setPermissions(new HashSet<>(testPermissions));
            roleRepository.save(testRole);

            System.out.println("Default roles created!");
        }

        // Kullanıcılar (Örnek: Admin ve Test)
        if (userRepository.count() == 0) {
            Organization adminOrg = organizationRepository.findByName(branchName1).orElse(null); // Örnek organizasyon
            Organization testOrg = organizationRepository.findByName(branchName2).orElse(null); // Örnek organizasyon

            Role adminRole = roleRepository.findByName(AuthRole.ADMIN.name()).orElse(null);
            User adminUser = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .email("admin")
                    .role(adminRole)
                    .authRole(AuthRole.ADMIN)
                    .organization(adminOrg)
                    .build();
            userRepository.save(adminUser);

            Role testRole = roleRepository.findByName(AuthRole.TEST.name()).orElse(null);
            User testUser = User.builder()
                    .username("test")
                    .password(passwordEncoder.encode("test"))
                    .email("test")
                    .role(testRole)
                    .authRole(AuthRole.TEST)
                    .organization(testOrg)
                    .build();
            userRepository.save(testUser);

            System.out.println("Default users created!");
        }

        // Kategoriler
        if (categoryRepository.count() == 0) {
            // Kayıt yoksa, yeni bir kategori ekle
            Category category = Category.builder()
                    .name("POS")
                    .orderValue(1)
                    .build();
            categoryRepository.save(category);
            System.out.println("Default category: 'POS' created!");
        }
//
//        // Kullanicilar
//        if (userRepository.count() == 0) {
//            // Kayıt yoksa, yeni bir kullanici ekle
//            List<User> defaultUsers = new ArrayList<>();
//            defaultUsers.add(User
//                    .builder()
//                    .email("admin")
//                    .password(passwordEncoder.encode("bb377261"))
//                    .authRole(AuthRole.ADMIN)
//                    .build());
//            defaultUsers.add(User
//                    .builder()
//                    .email("bahadir")
//                    .password(passwordEncoder.encode("bahadir"))
//                    .authRole(AuthRole.USER)
//                    .build());
//            defaultUsers.add(User
//                    .builder()
//                    .email("zeliha")
//                    .password(passwordEncoder.encode("zeliha"))
//                    .authRole(AuthRole.USER)
//                    .build());
//            userRepository.saveAll(defaultUsers);
//            System.out.println("Default users: " + defaultUsers.stream().map(User::getEmail).toList() + " created!");
//        }
    }
}