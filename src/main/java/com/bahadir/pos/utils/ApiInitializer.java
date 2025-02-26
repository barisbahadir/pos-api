package com.bahadir.pos.utils;

import com.bahadir.pos.entity.BaseStatus;
import com.bahadir.pos.entity.authentication.AuthenticationType;
import com.bahadir.pos.entity.category.Category;
import com.bahadir.pos.entity.company.Company;
import com.bahadir.pos.entity.organization.Organization;
import com.bahadir.pos.entity.permission.Permission;
import com.bahadir.pos.entity.permission.PermissionType;
import com.bahadir.pos.entity.role.Role;
import com.bahadir.pos.entity.user.User;
import com.bahadir.pos.entity.user.UserRole;
import com.bahadir.pos.repository.*;
import com.bahadir.pos.service.TwoFactorAuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
    private final TwoFactorAuthService twoFactorAuthService;

    public ApiInitializer(CompanyRepository companyRepository,
                          OrganizationRepository organizationRepository,
                          PermissionRepository permissionRepository,
                          RoleRepository roleRepository,
                          CategoryRepository categoryRepository,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          TwoFactorAuthService twoFactorAuthService) {
        this.companyRepository = companyRepository;
        this.organizationRepository = organizationRepository;
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.twoFactorAuthService = twoFactorAuthService;
    }

    @Override
    public void run(String... args) throws Exception {

        String companyName1 = "ANADOLUBANK AS";

        // Şirketler
        if (companyRepository.count() == 0) {
            Company company1 = Company.builder().name(companyName1).status(BaseStatus.ENABLE).orderValue(1).build();
            companyRepository.save(company1);
            System.out.println("Default companies created!");
        }

        // Organizasyonlar
        if (organizationRepository.count() == 0) {
            Company company1 = companyRepository.findByName(companyName1).orElse(null);
//            Company company2 = companyRepository.findByName(companyName2).orElse(null);

            Organization org1 = Organization.builder().name("Yazilim Gelistirme Departmani").status(BaseStatus.ENABLE).orderValue(1).company(company1).build();
            Organization org1_1 = Organization.builder().name("Internet Department").status(BaseStatus.ENABLE).orderValue(1).parent(org1).company(company1).build();
            Organization org1_2 = Organization.builder().name("Mobile Department").status(BaseStatus.ENABLE).orderValue(2).parent(org1).company(company1).build();
            Organization org1_3 = Organization.builder().name("ATM Department").status(BaseStatus.ENABLE).orderValue(3).parent(org1).company(company1).build();

            Organization org2 = Organization.builder().name("Insan Kaynaklari Departmani").status(BaseStatus.ENABLE).orderValue(2).company(company1).build();

            organizationRepository.saveAll(List.of(org1, org1_1, org1_2, org1_3, org2));
            System.out.println("Default organizations created!");
        }

        if (permissionRepository.count() == 0) {

            // Home Izinleri
            Permission home = Permission.builder().name("Home").label("sys.menu.home").icon("solar:home-outline").type(PermissionType.MENU).route("home").component("/home/index.tsx").orderValue(1).build();
            Permission pos = Permission.builder().name("Sale").label("sys.menu.sale").icon("solar:cart-large-2-outline").type(PermissionType.MENU).route("sale").component("/sale/index.tsx").orderValue(2).build();

            Permission products = Permission.builder().name("Products").label("sys.menu.products.index").icon("solar:document-add-linear").type(PermissionType.GROUP).route("product").orderValue(3).build();
            Permission productAdd = Permission.builder().name("Add Product").label("sys.menu.products.add").type(PermissionType.MENU).route("add").component("/products/add/index.tsx").orderValue(1).parent(products).build();
            Permission productEdit = Permission.builder().name("Edit Product").label("sys.menu.products.edit").type(PermissionType.MENU).route("edit/:id").component("/products/add/index.tsx").hide(true).orderValue(2).parent(products).build();
            Permission productsOrder = Permission.builder().name("Order Products").label("sys.menu.products.order").type(PermissionType.MENU).route("order").component("/products/order/index.tsx").orderValue(3).parent(products).build();
            Permission productList = Permission.builder().name("Product List").label("sys.menu.products.list").type(PermissionType.MENU).route("list").component("/products/list/index.tsx").orderValue(4).parent(products).build();

            Permission categories = Permission.builder().name("Categories").label("sys.menu.categories.index").icon("solar:bookmark-circle-outline").type(PermissionType.GROUP).route("category").orderValue(4).build();
            Permission categoryAdd = Permission.builder().name("Add Category").label("sys.menu.categories.add").type(PermissionType.MENU).route("add").component("/categories/add/index.tsx").orderValue(1).parent(categories).build();
            Permission categoryEdit = Permission.builder().name("Edit Category").label("sys.menu.categories.edit").type(PermissionType.MENU).route("edit/:id").component("/categories/add/index.tsx").hide(true).orderValue(2).parent(categories).build();
            Permission categoryOrder = Permission.builder().name("Order Categories").label("sys.menu.categories.order").type(PermissionType.MENU).route("order").component("/categories/order/index.tsx").orderValue(3).parent(categories).build();
            Permission categoryList = Permission.builder().name("Category List").label("sys.menu.categories.list").type(PermissionType.MENU).route("list").component("/categories/list/index.tsx").orderValue(4).parent(categories).build();

            // Dashboard İzinleri
//            Permission dashboard = Permission.builder().name("Dashboard").label("sys.menu.dashboard").icon("solar:chart-square-linear").type(PermissionType.GROUP).route("dashboard").orderValue(3).build();
//            Permission welcome = Permission.builder().name("Welcome").label("sys.menu.welcome").type(PermissionType.MENU).route("welcome").component("/dashboard/welcome/index.tsx").orderValue(1).parent(dashboard).build();
//            Permission analysis = Permission.builder().name("Analysis").label("sys.menu.analysis").type(PermissionType.MENU).route("analysis").component("/dashboard/analysis/index.tsx").orderValue(2).parent(dashboard).build();
//            Permission workbench = Permission.builder().name("Workbench").label("sys.menu.workbench").type(PermissionType.MENU).route("workbench").component("/dashboard/workbench/index.tsx").orderValue(3).parent(dashboard).build();

            // Management İzinleri
//            Permission management = Permission.builder().name("Management").label("sys.menu.management").icon("ic-management").type(PermissionType.GROUP).route("management").orderValue(5).build();
            Permission userIndex = Permission.builder().name("User Info").label("sys.menu.user.index").icon("solar:user-circle-linear").type(PermissionType.GROUP).route("user").orderValue(5).build();
            Permission userProfile = Permission.builder().name("Profile").label("sys.menu.user.profile").type(PermissionType.MENU).route("profile").component("/management/user/profile/index.tsx").orderValue(1).parent(userIndex).build();
            Permission userAccount = Permission.builder().name("Account").label("sys.menu.user.account").type(PermissionType.MENU).route("account").component("/management/user/account/index.tsx").orderValue(2).parent(userIndex).build();

            Permission systemIndex = Permission.builder().name("System").label("sys.menu.system.index").icon("solar:shield-user-outline").type(PermissionType.GROUP).route("system").orderValue(6).build();
            Permission systemOrganization = Permission.builder().name("Organization").label("sys.menu.system.organization").type(PermissionType.MENU).route("organization").component("/management/system/organization/index.tsx").orderValue(1).parent(systemIndex).build();
            Permission systemPermission = Permission.builder().name("Permission").label("sys.menu.system.permission").type(PermissionType.MENU).route("permission").component("/management/system/permission/index.tsx").orderValue(2).parent(systemIndex).build();
            Permission systemRole = Permission.builder().name("Role").label("sys.menu.system.role").type(PermissionType.MENU).route("role").component("/management/system/role/index.tsx").orderValue(3).parent(systemIndex).build();
            Permission systemUser = Permission.builder().name("User").label("sys.menu.system.user").type(PermissionType.MENU).route("user").component("/management/system/user/index.tsx").orderValue(4).parent(systemIndex).build();
            Permission systemUserDetail = Permission.builder().name("User Detail").label("sys.menu.system.user_detail").type(PermissionType.MENU).route("user/:id").component("/management/system/user/detail.tsx").hide(true).orderValue(5).parent(systemIndex).build();

            // Components İzinleri
            Permission components = Permission.builder().name("Components").label("sys.menu.components").icon("solar:widget-5-bold-duotone").type(PermissionType.GROUP).route("components").orderValue(7).build();
            Permission icon = Permission.builder().name("Icon").label("sys.menu.icon").type(PermissionType.MENU).route("icon").component("/components/icon/index.tsx").orderValue(1).parent(components).build();
            Permission animate = Permission.builder().name("Animate").label("sys.menu.animate").type(PermissionType.MENU).route("animate").component("/components/animate/index.tsx").orderValue(2).parent(components).build();
            Permission scroll = Permission.builder().name("Scroll").label("sys.menu.scroll").type(PermissionType.MENU).route("scroll").component("/components/scroll/index.tsx").orderValue(3).parent(components).build();
            Permission markdown = Permission.builder().name("Markdown").label("sys.menu.markdown").type(PermissionType.MENU).route("markdown").component("/components/markdown/index.tsx").orderValue(4).parent(components).build();
            Permission editor = Permission.builder().name("Editor").label("sys.menu.editor").type(PermissionType.MENU).route("editor").component("/components/editor/index.tsx").orderValue(5).parent(components).build();
            Permission multiLanguage = Permission.builder().name("Multi Language").label("sys.menu.i18n").type(PermissionType.MENU).route("i18n").component("/components/multi-language/index.tsx").orderValue(6).parent(components).build();
            Permission upload = Permission.builder().name("Upload").label("sys.menu.upload").type(PermissionType.MENU).route("upload").component("/components/upload/index.tsx").orderValue(7).parent(components).build();
            Permission chart = Permission.builder().name("Chart").label("sys.menu.chart").type(PermissionType.MENU).route("chart").component("/components/chart/index.tsx").orderValue(8).parent(components).build();
            Permission toast = Permission.builder().name("Toast").label("sys.menu.toast").type(PermissionType.MENU).route("toast").component("/components/toast/index.tsx").orderValue(9).parent(components).build();

            // Functions İzinleri
            Permission functions = Permission.builder().name("Functions").label("sys.menu.functions").icon("solar:plain-2-bold-duotone").type(PermissionType.GROUP).route("functions").orderValue(8).build();
            Permission clipboard = Permission.builder().name("Clipboard").label("sys.menu.clipboard").type(PermissionType.MENU).route("clipboard").component("/functions/clipboard/index.tsx").orderValue(1).parent(functions).build();
            Permission tokenExpired = Permission.builder().name("Token Expired").label("sys.menu.token_expired").type(PermissionType.MENU).route("token-expired").component("/functions/token-expired/index.tsx").orderValue(2).parent(functions).build();

            // Menu Level İzinleri
            Permission menuLevel = Permission.builder().name("Menu Level").label("sys.menu.menulevel.index").icon("ic-menulevel").type(PermissionType.GROUP).route("menu-level").orderValue(9).build();
            Permission menuLevel1a = Permission.builder().name("Menu Level 1a").label("sys.menu.menulevel.1a").type(PermissionType.MENU).route("menu-level-1a").component("/menu-level/menu-level-1a/index.tsx").orderValue(1).parent(menuLevel).build();
            Permission menuLevel1b = Permission.builder().name("Menu Level 1b").label("sys.menu.menulevel.1b.index").type(PermissionType.GROUP).route("menu-level-1b").orderValue(2).parent(menuLevel).build();
            Permission menuLevel2a = Permission.builder().name("Menu Level 2a").label("sys.menu.menulevel.1b.2a").type(PermissionType.MENU).route("menu-level-2a").component("/menu-level/menu-level-1b/menu-level-2a/index.tsx").orderValue(1).parent(menuLevel1b).build();
            Permission menuLevel2b = Permission.builder().name("Menu Level 2b").label("sys.menu.menulevel.1b.2b.index").type(PermissionType.GROUP).route("menu-level-2b").orderValue(2).parent(menuLevel1b).build();
            Permission menuLevel3a = Permission.builder().name("Menu Level 3a").label("sys.menu.menulevel.1b.2b.3a").type(PermissionType.MENU).route("menu-level-3a").component("/menu-level/menu-level-1b/menu-level-2b/menu-level-3a/index.tsx").orderValue(1).parent(menuLevel2b).build();
            Permission menuLevel3b = Permission.builder().name("Menu Level 3b").label("sys.menu.menulevel.1b.2b.3b").type(PermissionType.MENU).route("menu-level-3b").component("/menu-level/menu-level-1b/menu-level-2b/menu-level-3b/index.tsx").orderValue(2).parent(menuLevel2b).build();

            Permission calendar = Permission.builder()
                    .name("Calendar")
                    .label("sys.menu.calendar")
                    .icon("solar:calendar-bold-duotone")
                    .type(PermissionType.MENU)
                    .route("calendar")
                    .component("/system/others/calendar/index.tsx")
                    .orderValue(10)
                    .build();

            Permission kanban = Permission.builder()
                    .name("kanban")
                    .label("sys.menu.kanban")
                    .icon("solar:clipboard-bold-duotone")
                    .type(PermissionType.MENU)
                    .route("kanban")
                    .component("/system/others/kanban/index.tsx")
                    .orderValue(7)
                    .build();

            Permission disabled = Permission.builder()
                    .name("Disabled")
                    .label("sys.menu.disabled")
                    .icon("ic_disabled")
                    .type(PermissionType.MENU)
                    .route("disabled")
                    .status(BaseStatus.DISABLE)
                    .component("/system/others/calendar/index.tsx")
                    .orderValue(11)
                    .build();

            Permission label = Permission.builder()
                    .name("Label")
                    .label("sys.menu.label")
                    .icon("ic_label")
                    .type(PermissionType.MENU)
                    .route("label")
                    .newFeature(true)
                    .component("/system/others/blank.tsx")
                    .orderValue(12)
                    .build();

            Permission frame = Permission.builder()
                    .name("Frame")
                    .label("sys.menu.frame")
                    .icon("ic_external")
                    .type(PermissionType.GROUP)
                    .route("frame")
                    .orderValue(13)
                    .build();

            Permission externalLink = Permission.builder()
                    .name("External Link")
                    .label("sys.menu.external_link")
                    .type(PermissionType.MENU)
                    .route("external_link")
                    .hideTab(true)
                    .component("/system/others/iframe/external-link.tsx")
                    .frameSrc("https://ant.design/")
                    .parent(frame)
                    .orderValue(14)
                    .build();

            Permission iframe = Permission.builder()
                    .name("Iframe")
                    .label("sys.menu.iframe")
                    .type(PermissionType.MENU)
                    .route("frame")
                    .component("/system/others/iframe/index.tsx")
                    .frameSrc("https://ant.design/")
                    .parent(frame)
                    .orderValue(15)
                    .build();

            Permission blank = Permission.builder()
                    .name("Blank")
                    .label("sys.menu.blank")
                    .icon("ic_blank")
                    .type(PermissionType.MENU)
                    .route("blank")
                    .component("/system/others/blank.tsx")
                    .orderValue(16)
                    .build();

            permissionRepository.saveAll(List.of(home, pos,
                    products, productAdd, productEdit, productsOrder, productList,
                    categories, categoryAdd, categoryEdit, categoryOrder, categoryList, /*dashboard, welcome, analysis, workbench, management,*/ userIndex, userProfile, userAccount, systemIndex, systemOrganization, systemPermission, systemRole, systemUser, systemUserDetail, components, icon, animate, scroll, markdown, editor, multiLanguage, upload, chart, toast, functions, clipboard, tokenExpired, menuLevel, menuLevel1a, menuLevel1b, menuLevel2a, menuLevel2b, menuLevel3a, menuLevel3b, calendar, kanban, disabled, label, frame, externalLink, iframe, blank));

            System.out.println("Default permissions created!");
        }

        // Roller (Örnek: Admin ve Test)
        if (roleRepository.count() == 0) {
            Role adminRole = Role.builder()
                    .name(UserRole.ADMIN.name())
                    .label(UserRole.ADMIN.name())
                    .status(BaseStatus.ENABLE)
                    .orderValue(1)
                    .description("Super Admin")
                    .build();
            // Admin rolüne tüm izinleri ekleyin
            adminRole.setPermissions(new HashSet<>(permissionRepository.findAll()));
            roleRepository.save(adminRole);

            Role testRole = Role.builder()
                    .name(UserRole.TEST.name())
                    .label(UserRole.TEST.name())
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
            Organization adminOrg = organizationRepository.findByName("Internet Department").orElse(null); // Örnek organizasyon
            Organization testOrg = organizationRepository.findByName("Insan Kaynaklari Departmani").orElse(null); // Örnek organizasyon

            Role adminRole = roleRepository.findByName(UserRole.ADMIN.name()).orElse(null);
            User adminUser = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .email("admin")
                    .role(adminRole)
                    .authRole(UserRole.ADMIN)
                    .organization(adminOrg)
                    .authType(AuthenticationType.NONE)
                    .build();
            userRepository.save(adminUser);

            String testEmail = "test";
            String secretKey = twoFactorAuthService.generateSecretKey();

            Role testRole = roleRepository.findByName(UserRole.TEST.name()).orElse(null);
            User testUser = User.builder()
                    .username(testEmail)
                    .password(passwordEncoder.encode(testEmail))
                    .email(testEmail)
                    .role(adminRole)
                    .authRole(UserRole.ADMIN)
                    .organization(testOrg)
                    .authType(AuthenticationType.OTP)
                    .twoFactorAuthSecretKey(secretKey)
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