package com.springboot.app.config;

import com.springboot.app.accounts.entity.*;
import com.springboot.app.accounts.enumeration.AccountStatus;
import com.springboot.app.accounts.enumeration.RoleName;
import com.springboot.app.accounts.repository.RoleRepository;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.bagdes.Badge;
import com.springboot.app.bagdes.BadgeService;
import com.springboot.app.bannedKeyword.BannedKeyWordService;
import com.springboot.app.bannedKeyword.BannedKeyword;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.emails.entity.EmailOption;
import com.springboot.app.emails.entity.RegistrationOption;
import com.springboot.app.emails.repository.EmailOptionRepository;
import com.springboot.app.forums.entity.*;
import com.springboot.app.forums.repository.ForumGroupRepository;
import com.springboot.app.forums.service.DiscussionService;
import com.springboot.app.forums.service.ForumService;
import com.springboot.app.service.GenericService;
import com.springboot.app.tags.Tag;
import com.springboot.app.tags.TagService;
import com.springboot.app.utils.JSFUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class DatabaseInit {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInit.class);
    /*
     * Default values on first time system initialization
     */
    private static final String REGISTRATION_EMAIL_SUBJECT = "Confirm account registration at TechForum";
    private static final String REGISTRATION_EMAIL_TEMPLATE = "<p><strong>Hi #username</strong>,</p>"
                                                              + "<p>This email&nbsp;<strong>#email</strong>&nbsp;has been used for account registration on <strong>TechForum</strong>.</p>"
                                                              + "<p>If that wasn&#39;t your intention, kindly ignore this email. Otherwise, please lick on this link #confirm-url to activate your account.</p>"
                                                              + "<p>Regards,</p>";
    private static final String PASSWORD_RESET_EMAIL_SUBJECT = "Password reset requested at TechForum";
    private static final String PASSWORD_RESET_EMAIL_TEMPLATE = "<p><strong>Hi #username</strong>,</p>"
                                                                + "<p>Here is the <strong>#reset-url</strong> to reset your password in <strong>TechForum</strong></p>"
                                                                + "<p>If you didn&#39;t request this, kindly ignore this email.</p>" + "<p>Regards,</p>";
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private ForumService forumService;
    @Autowired
    private DiscussionService discussionService;
    @Autowired
    private TagService tagService;
    @Autowired
    private GenericService genericService;
    @Autowired
    private EmailOptionRepository emailOptionRepository;
    @Autowired
    private BadgeService badgeService;
    @Autowired
    private BannedKeyWordService bannedKeyWordService;
    @Autowired
    private ForumGroupRepository forumGroupRepository;

    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository, UserRepository userRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                logger.info("Database is running...");
                addRoles(roleRepository);
                addAdmin(userRepository, roleRepository);

                if (forumGroupRepository.findAll().isEmpty()) {
                    createForumGroup("admin");
                }

                createRegistrationOption();
                createEmailOption();
                createCommentOption();
                createAvatarOption();
                createBadgeDefault();

                if (bannedKeyWordService.getAllBannedKeywords().getDataObject().isEmpty()) {
                    createBannedKeyword();
                }

            }
        };
    }

    private void addRoles(RoleRepository roleRepository) {
        List<Role> roles = roleRepository.findAll();
        if (!roles.isEmpty()) {
            return;
        }
        roleRepository.save(new Role(1, RoleName.ROLE_ADMIN));
        roleRepository.save(new Role(2, RoleName.ROLE_USER));
        roleRepository.save(new Role(3, RoleName.ROLE_MODERATOR));
        logger.info("Roles added to the database.");
    }

    private void addAdmin(UserRepository userRepository, RoleRepository roleRepository) {
        List<Role> roleList = roleRepository.findAll();
        if (roleList.isEmpty()) {
            return;
        }
        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            return;
        }
        User ad = new User();

        ad.setUsername("admin");
        ad.setEmail("admin@gmail.com");
        ad.setPassword(encoder.encode("Admin@123"));

        Set<Role> r = roleList.stream().collect(HashSet::new, HashSet::add, HashSet::addAll);
        ad.setRoles(r);
        ad.setAccountStatus(AccountStatus.ACTIVE);

        Person p = new Person();
        p.setFirstName("System");
        p.setLastName("Admin");
        p.setPhone("1234567890");
        p.setAddress("Ho Chi Minh City, Vietnam");
        p.setBio("Admin Bio");

        ad.setPerson(p);

        UserStat userStat = new UserStat();
        userStat.setCreatedBy(ad.getUsername());
        ad.setStat(userStat);

        userRepository.save(ad);

    }

    private void createBannedKeyword() {
        BannedKeyword bannedKeyword = new BannedKeyword();
        bannedKeyword.setKeyword("fuck");
        bannedKeyWordService.saveBannedKeyword(bannedKeyword);

        BannedKeyword bannedKeyword2 = new BannedKeyword();
        bannedKeyword2.setKeyword("lol");
        bannedKeyWordService.saveBannedKeyword(bannedKeyword2);

        BannedKeyword bannedKeyword3 = new BannedKeyword();
        bannedKeyword3.setKeyword("dcm");
        bannedKeyWordService.saveBannedKeyword(bannedKeyword3);

        logger.info("Bulletin tag created.");
    }

    private void createBulletinTag(Discussion discussion) {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setLabel("Bulletin");
        tag.setIcon("FaGamepad");
        tag.setColor("#9F5B5B");

        tagService.createNewTag(tag);

        discussion.setTags(List.of(tag));
        genericService.updateEntity(discussion);
        logger.info("Bulletin tag created.");
    }

    //create forum group
    private void createForumGroup(String username) {
        ForumGroup forumGroup1 = new ForumGroup();
        forumGroup1.setTitle("System");
        forumGroup1.setCreatedBy(username);
        forumGroup1.setIcon("FaLaptopCode");
        forumGroup1.setColor("#1ABB76");
        forumService.addForumGroup(forumGroup1, username);

        ForumGroup forumGroup2 = new ForumGroup();
        forumGroup2.setTitle("Technology");
        forumGroup2.setCreatedBy(username);
        forumGroup2.setIcon("FaServer");
        forumGroup2.setColor("#CC363B");
        forumService.addForumGroup(forumGroup2, username);

        ForumGroup forumGroup3 = new ForumGroup();
        forumGroup3.setTitle("Gaming");
        forumGroup3.setCreatedBy(username);
        forumGroup3.setIcon("IoGameController");
        forumGroup3.setColor("#2964D4");
        forumService.addForumGroup(forumGroup3, username);

        logger.info("Forum group created.");

        createAnouncementsForum1(username, forumGroup1);
        createAnouncementsForum2(username, forumGroup2);
        createAnouncementsForum3(username, forumGroup3);
    }

    private void createAnouncementsForum1(String username, ForumGroup forumGroup) {
        Forum forum1 = new Forum();
        forum1.setCreatedBy(username);
        forum1.setTitle("Feedback");
        forum1.setDescription("Helpful that is given to someone to say what can be done to improve a performance\n");
        forum1.setIcon("FaFileAlt");
        forum1.setColor("#4ADA78");
        ForumGroup newForumGroup = genericService.findEntity(ForumGroup.class, forumGroup.getId()).getDataObject();
        forumService.addForum(forum1, newForumGroup, username);

        Forum forum2 = new Forum();
        forum2.setCreatedBy(username);
        forum2.setTitle("Update the policy");
        forum2.setDescription("We can also detect the rules responsible for the conflict and then update the policy with new deadlines for these rules.");
        forum2.setIcon("FaBook");
        forum2.setColor("9F5B5B");
        ForumGroup newForumGroup2 = genericService.findEntity(ForumGroup.class, forumGroup.getId()).getDataObject();
        forumService.addForum(forum2, newForumGroup2, username);

        logger.info("Announcements forum created.");

        createWelcomeDiscussion1(username, forum1);
        createWelcomeDiscussion2(username, forum2);
    }

    private void createAnouncementsForum2(String username, ForumGroup forumGroup) {
        Forum forum3 = new Forum();
        forum3.setCreatedBy(username);
        forum3.setTitle("Coding");
        forum3.setDescription("Coding is the process of creating instructions that computers then interpret and follow.");
        forum3.setIcon("FaCode");
        forum3.setColor("9F5B5B");
        ForumGroup newForumGroup = genericService.findEntity(ForumGroup.class, forumGroup.getId()).getDataObject();
        forumService.addForum(forum3, newForumGroup, username);

        Forum forum4 = new Forum();
        forum4.setCreatedBy(username);
        forum4.setTitle("Backend");
        forum4.setDescription("The back-end is the code that runs on the server, that receives requests from the clients, and contains the logic to send the appropriate data back to the client.");
        forum4.setIcon("FaServer");
        forum4.setColor("9F5B5B");
        ForumGroup newForumGroup2 = genericService.findEntity(ForumGroup.class, forumGroup.getId()).getDataObject();
        forumService.addForum(forum4, newForumGroup2, username);

        Forum forum5 = new Forum();
        forum5.setCreatedBy(username);
        forum5.setTitle("Frontend");
        forum5.setIcon("FaServer");
        forum5.setDescription("Front-end development describes the part of an app or website that customers interact with directly.");
        forum5.setColor("9F5B5B");
        ForumGroup newForumGroup3 = genericService.findEntity(ForumGroup.class, forumGroup.getId()).getDataObject();
        forumService.addForum(forum5, newForumGroup3, username);

        logger.info("Announcements forum created.");

    }

    private void createAnouncementsForum3(String username, ForumGroup forumGroup) {
        Forum forum5 = new Forum();
        forum5.setCreatedBy(username);
        forum5.setTitle("RBP Gaming");
        forum5.setDescription("A role-playing game (RPG) is a game in which each participant assumes the role of a character that can interact within the game's imaginary world.");
        forum5.setIcon("FaRobot");
        forum5.setColor("9F5B5B");
        ForumGroup newForumGroup = genericService.findEntity(ForumGroup.class, forumGroup.getId()).getDataObject();
        forumService.addForum(forum5, newForumGroup, username);

        Forum forum6 = new Forum();
        forum6.setCreatedBy(username);
        forum6.setTitle("Streamers");
        forum6.setDescription("Game streamers broadcast themselves playing video games online.");
        forum6.setIcon("FaMicrochip");
        forum6.setColor("9F5B5B");
        ForumGroup newForumGroup2 = genericService.findEntity(ForumGroup.class, forumGroup.getId()).getDataObject();
        forumService.addForum(forum6, newForumGroup2, username);

        logger.info("Announcements forum created.");
    }

    private void createWelcomeDiscussion1(String username, Forum forum) {
        //Discussion feedback
        Discussion discussion1 = new Discussion();
        discussion1.setForum(forum);
        discussion1.setCreatedBy(username);
        discussion1.setClosed(true);
        discussion1.setSticky(true);
        discussion1.setImportant(true);
        discussion1.setTitle("We listened to your feedback, we took it to heart");
        forum.getDiscussions().add(discussion1);

        Comment comment1 = new Comment();
        comment1.setContent("We will continue to explore the best experience for our users, listening to your feedback and looking at how we can meet your needs in the best way possible.");
        comment1.setIpAddress(JSFUtils.getRemoteIPAddress());

        discussionService.addDiscussion(discussion1, comment1, "admin");
        createBulletinTag(discussion1);

        logger.info("Welcome discussion created.");

    }

    private void createWelcomeDiscussion2(String username, Forum forum) {
        //Update the policy
        Discussion discussion3 = new Discussion();
        discussion3.setForum(forum);
        forum.getDiscussions().add(discussion3);
        discussion3.setCreatedBy(username);
        discussion3.setClosed(true);
        discussion3.setSticky(true);
        discussion3.setImportant(true);
        discussion3.setTitle("The policy statement needs to be revised");


        Comment comment3 = new Comment();
        comment3.setContent("Spotify CEO Daniel Ek soon apologized, and said the company would update the policy to better clarify how the permissions will be used.");
        comment3.setIpAddress(JSFUtils.getRemoteIPAddress());

        discussionService.addDiscussion(discussion3, comment3, "admin");

        logger.info("Welcome discussion created.");

    }

    private void createEmailOption() {
        try {
            EmailOption emailOption = emailOptionRepository.findById(1L).orElse(null);
            if (emailOption != null) {
                logger.info("Email option already exists.");
            } else {
                emailOption = new EmailOption();
                emailOption.setId(1L);
                emailOption.setCreatedBy("admin");
                emailOption.setHost("smtp.gmail.com");
                emailOption.setPort(587);
                emailOption.setUsername("techforum1368@gmail.com");
                emailOption.setPassword("hqsjdkrlaujgcxrk");
                emailOption.setTlsEnable(true);
                emailOption.setAuthentication(true);

                emailOptionRepository.save(emailOption);
                logger.info("Email option created.");
            }
        } catch (Exception e) {
            logger.error("Error creating email option: " + e.getMessage());
        }
    }

    public void createRegistrationOption() {
        RegistrationOption registrationOption = genericService.getEntity(RegistrationOption.class, 1L).getDataObject();
        if (registrationOption != null) {
            logger.info("Registration option already exists.");
        } else {
            logger.info("Registration option created.");
            registrationOption = new RegistrationOption();
            registrationOption.setId(1L);
            registrationOption.setCreatedBy("admin");
            registrationOption.setUpdatedBy("admin");
            registrationOption.setEnableEmailConfirm(true);

            registrationOption.setRegistrationEmailSubject(REGISTRATION_EMAIL_SUBJECT);
            registrationOption.setRegistrationEmailTemplate(REGISTRATION_EMAIL_TEMPLATE);
            registrationOption.setPasswordResetEmailSubject(PASSWORD_RESET_EMAIL_SUBJECT);
            registrationOption.setPasswordResetEmailTemplate(PASSWORD_RESET_EMAIL_TEMPLATE);

            registrationOption.setCreatedAt(LocalDateTime.now());
            genericService.saveEntity(registrationOption);
        }
    }

    private void createCommentOption() {
        CommentOption commentOption = genericService.getEntity(CommentOption.class, 1L).getDataObject();
        if (commentOption != null) {
            logger.info("Comment option already exists.");
        } else {
            commentOption = new CommentOption();
            commentOption.setId(1L);
            commentOption.setCreatedBy("admin");
            commentOption.setUpdatedBy("admin");

            // default values
            commentOption.setMinCharDiscussionTitle(1);
            commentOption.setMaxCharDiscussionTitle(80);
            commentOption.setMinCharDiscussionContent(1);
            commentOption.setMaxCharDiscussionContent(10000 * 1024); // 10,2400KB ~ 10MB
            commentOption.setMaxDiscussionThumbnail(5);
            commentOption.setMaxDiscussionAttachment(5);
            commentOption.setMaxByteDiscussionThumbnail(5000 * 1024); // 5120KB ~ 5MB
            commentOption.setMaxByteDiscussionAttachment(5000 * 1024); // 5120KB ~ 5MB
            commentOption.setAllowDiscussionTitleEdit(true);

            commentOption.setMinCharCommentTitle(1);
            commentOption.setMaxCharCommentTitle(80);
            commentOption.setMinCharCommentContent(1);
            commentOption.setMaxCharCommentContent(10000 * 1024); // 10,000KB ~ 10MB
            commentOption.setMaxCommentThumbnail(3);
            commentOption.setMaxCommentAttachment(3);
            commentOption.setMaxByteCommentThumbnail(5000 * 1024); // 1000KB ~ 5MB
            commentOption.setMaxByteCommentAttachment(5000 * 1024); // 1000KB ~ 5MB
            commentOption.setAllowCommentEdit(true);

            genericService.saveEntity(commentOption);
            logger.info("Comment option created.");
        }
    }

    private void createAvatarOption() {

        AvatarOption avatarOption = genericService.getEntity(AvatarOption.class, 1L).getDataObject();
        if (avatarOption != null) {
            logger.info("Avatar option already exists.");
        } else {
            avatarOption = new AvatarOption();
            avatarOption.setId(1L);
            avatarOption.setMaxFileSize(5 * 1024); // ~500KB
            avatarOption.setMaxWidth(1000);
            avatarOption.setMaxHeight(1000);

            avatarOption.setCreatedBy("admin");
            avatarOption.setUpdatedBy("admin");
            genericService.saveEntity(avatarOption);
            logger.info("Avatar option created.");
        }
    }

    private void createBadgeDefault() {
        ServiceResponse<List<Badge>> response = badgeService.getAllBadges();
        if (response.getDataObject() != null && !response.getDataObject().isEmpty()) {
            logger.info("Badge default already exists.");
        } else {
            logger.info("Badge default not exists. Create badge default.");
            badgeService.createBadge();
            badgeService.createTraineeBadge();
            badgeService.createBronzeBadge();
            badgeService.createSilverBadge();
            badgeService.createGoldBadge();
            badgeService.createPlatinumBadge();
            logger.info("Badge default created.");
        }
    }

}
