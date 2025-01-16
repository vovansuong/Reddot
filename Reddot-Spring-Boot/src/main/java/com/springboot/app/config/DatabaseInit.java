package com.springboot.app.config;

import com.springboot.app.accounts.entity.*;
import com.springboot.app.accounts.enumeration.AccountStatus;
import com.springboot.app.accounts.enumeration.RoleName;
import com.springboot.app.accounts.repository.AvatarOptionRepository;
import com.springboot.app.accounts.repository.RoleRepository;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.bagdes.BadgeRepository;
import com.springboot.app.bagdes.BadgeService;
import com.springboot.app.bannedKeyword.BannedKeyWordService;
import com.springboot.app.bannedKeyword.BannedKeyword;
import com.springboot.app.bannedKeyword.BannedKeywordRepository;
import com.springboot.app.email.entity.EmailContentOption;
import com.springboot.app.email.entity.EmailOptionType;
import com.springboot.app.email.repository.EmailContentOptionRepository;
import com.springboot.app.forums.entity.*;
import com.springboot.app.forums.repository.CommentOptionRepository;
import com.springboot.app.forums.repository.ForumGroupRepository;
import com.springboot.app.forums.service.DiscussionService;
import com.springboot.app.forums.service.ForumService;
import com.springboot.app.service.GenericService;
import com.springboot.app.tags.Tag;
import com.springboot.app.tags.TagRepository;
import com.springboot.app.tags.TagService;
import com.springboot.app.utils.JSFUtils;
import com.springboot.app.utils.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DatabaseInit implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInit.class);
    private static final String OAUTH_REGISTRATION_NOTIFICATTION_TEMPLATE = ResourceUtil.getOAuthRegistrationNotificationTemplate();
    private final String PASSWORD_RESET_EMAIL_TEMPLATE = ResourceUtil.getPasswordForgotTemplate();
    private final String REGISTRATION_EMAIL_TEMPLATE = ResourceUtil.getRegistrationTemplate();
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
    private BadgeService badgeService;
    @Autowired
    private BannedKeyWordService bannedKeyWordService;
    @Autowired
    private ForumGroupRepository forumGroupRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private EmailContentOptionRepository mailContentOptionRepository;
    @Autowired
    private CommentOptionRepository commentOptionRepository;
    @Autowired
    private AvatarOptionRepository avatarOptionRepository;
    @Autowired
    private BannedKeywordRepository bannedKeywordRepository;
    @Autowired
    private BadgeRepository badgeRepository;

    @Transactional
    @Override
    public void run(String... args) {
        logger.info("Database is running...");
        if (roleRepository.findAll().isEmpty()) {
            addRoles();
        }
        if (userRepository.findAll().isEmpty()) {
            addAdmin();
        }
        if (forumGroupRepository.findAll().isEmpty()) {
            createForumGroup();
        }
        if (mailContentOptionRepository.findAll().isEmpty()) {
            seedMailContentOption();
        }
        if (commentOptionRepository.findAll().isEmpty()) {
            createCommentOption();
        }
        if (avatarOptionRepository.findAll().isEmpty()) {
            createAvatarOption();
        }
        if (badgeRepository.findAll().isEmpty()) {
            createBadgeDefault();
        }
        if (tagRepository.findAll().isEmpty()) {
            initTags();
        }
        if (bannedKeywordRepository.findAll().isEmpty()) {
            createBannedKeyword();
        }
    }

    private void addRoles() {
        List<Role> roles = List.of(
                new Role(RoleName.ROLE_USER),
                new Role(RoleName.ROLE_ADMIN),
                new Role(RoleName.ROLE_MODERATOR)
        );
        roleRepository.saveAll(roles);
        logger.info("Roles added to the database.");
    }

    private void addAdmin() {
        List<Role> roleList = roleRepository.findAll();
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
        bannedKeyword.setKeyword("bad");
        bannedKeyWordService.saveBannedKeyword(bannedKeyword);
        BannedKeyword bannedKeyword2 = new BannedKeyword();
        bannedKeyword2.setKeyword("lol");
        bannedKeyWordService.saveBannedKeyword(bannedKeyword2);
        BannedKeyword bannedKeyword3 = new BannedKeyword();
        bannedKeyword3.setKeyword("fuck");
        bannedKeyWordService.saveBannedKeyword(bannedKeyword3);
    }

    private void initTags() {
        List<String> tags = List.of(
                "java", "spring", "spring-boot", "spring-security", "hibernate", "jpa", "thymeleaf", "html", "css", "javascript", "jquery", "react",
                "angular", "vuejs", "nodejs", "expressjs", "mongodb", "mysql", "postgresql", "mariadb", "docker", "kubernetes", "jenkins", "git", "github", "gitlab", "bitbucket",
                "maven", "gradle", "intellij-idea", "eclipse", "netbeans", "visual-studio-code", "atom", "sublime-text", "vim", "emacs", "android", "ios", "kotlin", "swift", "react-native",
                "flutter", "ionic", "xamarin", "phonegap", "cordova", "pwa", "webassembly", "webgl", "unity3d", "unreal-engine", "blender", "gimp", "inkscape", "photoshop", "illustrator",
                "figma", "sketch", "xd", "zeplin", "invision", "jira", "trello", "asana", "slack", "discord", "microsoft-teams", "zoom", "google-meet", "webex", "skype", "whatsapp", "telegram",
                "signal", "facebook", "twitter", "instagram", "linkedin", "pinterest", "tiktok", "snapchat", "youtube", "twitch", "netflix", "amazon-prime", "hbo", "disney-plus", "spotify",
                "apple-music", "google-play-music", "deezer", "soundcloud", "tunein", "pandora", "youtube-music", "amazon-music", "shazam", "google-play-store", "apple-app-store",
                "microsoft-store", "amazon-app-store", "google-cloud", "aws", "azure", "heroku", "digitalocean", "linode", "vultr", "upcloud", "cloudflare", "fastly", "akamai", "cloudfront",
                "s3", "rds", "ec2", "lambda", "sqs", "s3", "route-53", "elasticache", "dynamodb", "cloudwatch", "cloudtrail", "cloudformation", "cloudfront", "cloudsearch", "cloudhsm");
        tags.forEach(label -> {
            tagService.createNewTag(new Tag(label, "FaGamepad", "#9F5B5B"));
        });
        logger.info("Bulletin tag created.");
    }

    private void createForumGroup() {
        ForumGroup forumGroup1 = new ForumGroup();
        forumGroup1.setTitle("System");
        forumGroup1.setCreatedBy("admin");
        forumGroup1.setIcon("FaLaptopCode");
        forumGroup1.setColor("#1ABB76");
        forumService.addForumGroup(forumGroup1, "admin");

        ForumGroup forumGroup2 = new ForumGroup();
        forumGroup2.setTitle("Technology");
        forumGroup2.setCreatedBy("admin");
        forumGroup2.setIcon("FaServer");
        forumGroup2.setColor("#CC363B");
        forumService.addForumGroup(forumGroup2, "admin");

        ForumGroup forumGroup3 = new ForumGroup();
        forumGroup3.setTitle("Gaming");
        forumGroup3.setCreatedBy("admin");
        forumGroup3.setIcon("IoGameController");
        forumGroup3.setColor("#2964D4");
        forumService.addForumGroup(forumGroup3, "admin");

        logger.info("Forum group created.");

        createAnnouncementForum1(forumGroup1);
        createAnnouncementForum2(forumGroup2);
        createAnnouncementForum3(forumGroup3);
    }

    private void createAnnouncementForum1(ForumGroup forumGroup) {
        Forum forum1 = new Forum();
        forum1.setCreatedBy("admin");
        forum1.setTitle("Feedback");
        forum1.setDescription("Helpful that is given to someone to say what can be done to improve a performance\n");
        forum1.setIcon("FaFileAlt");
        forum1.setColor("#4ADA78");
        ForumGroup newForumGroup = genericService.findEntity(ForumGroup.class, forumGroup.getId()).getDataObject();
        forumService.addForum(forum1, newForumGroup, "admin");
        Forum forum2 = new Forum();
        forum2.setCreatedBy("admin");
        forum2.setTitle("Update the policy");
        forum2.setDescription("We can also detect the rules responsible for the conflict and then update the policy with new deadlines for these rules.");
        forum2.setIcon("FaBook");
        forum2.setColor("9F5B5B");
        ForumGroup newForumGroup2 = genericService.findEntity(ForumGroup.class, forumGroup.getId()).getDataObject();
        forumService.addForum(forum2, newForumGroup2, "admin");
        createWelcomeDiscussion1(forum1);
        createWelcomeDiscussion2(forum2);
    }

    private void createAnnouncementForum2(ForumGroup forumGroup) {
        Forum forum3 = new Forum();
        forum3.setCreatedBy("admin");
        forum3.setTitle("Coding");
        forum3.setDescription("Coding is the process of creating instructions that computers then interpret and follow.");
        forum3.setIcon("FaCode");
        forum3.setColor("9F5B5B");
        ForumGroup newForumGroup = genericService.findEntity(ForumGroup.class, forumGroup.getId()).getDataObject();
        forumService.addForum(forum3, newForumGroup, "admin");

        Forum forum4 = new Forum();
        forum4.setCreatedBy("admin");
        forum4.setTitle("Backend");
        forum4.setDescription("The back-end is the code that runs on the server, that receives requests from the clients, and contains the logic to send the appropriate data back to the client.");
        forum4.setIcon("FaServer");
        forum4.setColor("9F5B5B");
        ForumGroup newForumGroup2 = genericService.findEntity(ForumGroup.class, forumGroup.getId()).getDataObject();
        forumService.addForum(forum4, newForumGroup2, "admin");

        Forum forum5 = new Forum();
        forum5.setCreatedBy("admin");
        forum5.setTitle("Frontend");
        forum5.setIcon("FaServer");
        forum5.setDescription("Front-end development describes the part of an app or website that customers interact with directly.");
        forum5.setColor("9F5B5B");
        ForumGroup newForumGroup3 = genericService.findEntity(ForumGroup.class, forumGroup.getId()).getDataObject();
        forumService.addForum(forum5, newForumGroup3, "admin");
    }

    private void createAnnouncementForum3(ForumGroup forumGroup) {
        Forum forum5 = new Forum();
        forum5.setCreatedBy("admin");
        forum5.setTitle("RBP Gaming");
        forum5.setDescription("A role-playing game (RPG) is a game in which each participant assumes the role of a character that can interact within the game's imaginary world.");
        forum5.setIcon("FaRobot");
        forum5.setColor("9F5B5B");
        ForumGroup newForumGroup = genericService.findEntity(ForumGroup.class, forumGroup.getId()).getDataObject();
        forumService.addForum(forum5, newForumGroup, "admin");

        Forum forum6 = new Forum();
        forum6.setCreatedBy("admin");
        forum6.setTitle("Streamers");
        forum6.setDescription("Game streamers broadcast themselves playing video games online.");
        forum6.setIcon("FaMicrochip");
        forum6.setColor("9F5B5B");
        ForumGroup newForumGroup2 = genericService.findEntity(ForumGroup.class, forumGroup.getId()).getDataObject();
        forumService.addForum(forum6, newForumGroup2, "admin");

        logger.info("Announcements forum created.");
    }

    private void createWelcomeDiscussion1(Forum forum) {
        //Discussion feedback
        Discussion discussion1 = new Discussion();
        discussion1.setForum(forum);
        discussion1.setCreatedBy("admin");
        discussion1.setClosed(true);
        discussion1.setSticky(true);
        discussion1.setImportant(true);
        discussion1.setTitle("We listened to your feedback, we took it to heart");
        forum.getDiscussions().add(discussion1);

        Comment comment1 = new Comment();
        comment1.setContent("We will continue to explore the best experience for our users, listening to your feedback and looking at how we can meet your needs in the best way possible.");
        comment1.setIpAddress(JSFUtils.getRemoteIPAddress());

        discussionService.addDiscussion(discussion1, comment1, "admin");

        logger.info("Welcome discussion created.");

    }

    private void createWelcomeDiscussion2(Forum forum) {
        //Update the policy
        Discussion discussion3 = new Discussion();
        discussion3.setForum(forum);
        forum.getDiscussions().add(discussion3);
        discussion3.setCreatedBy("admin");
        discussion3.setClosed(true);
        discussion3.setSticky(true);
        discussion3.setImportant(true);
        discussion3.setTitle("The policy statement needs to be revised");
        Comment comment3 = new Comment();
        comment3.setContent("Spotify CEO Daniel Ek soon apologized, and said the company would update the policy to better clarify how the permissions will be used.");
        comment3.setIpAddress(JSFUtils.getRemoteIPAddress());
        discussionService.addDiscussion(discussion3, comment3, "admin");
    }

    public void seedMailContentOption() {
        logger.info("Registration option created.");
        List<EmailContentOption> options = List.of(
                new EmailContentOption(EmailOptionType.REGISTRATION_CONFIRMATION, "Reddot Account Confirmation", REGISTRATION_EMAIL_TEMPLATE),
                new EmailContentOption(EmailOptionType.PASSWORD_RESET, "Reddot Password Reset Request", PASSWORD_RESET_EMAIL_TEMPLATE),
                new EmailContentOption(EmailOptionType.REGISTRATION_WITH_OAUTH_NOTIFICATION, "Reddot Account Created", OAUTH_REGISTRATION_NOTIFICATTION_TEMPLATE)
        );
        mailContentOptionRepository.saveAll(options);
    }

    private void createCommentOption() {
        CommentOption commentOption = new CommentOption();
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

        commentOptionRepository.save(commentOption);
        logger.info("Comment option created.");
    }

    private void createAvatarOption() {
        AvatarOption avatarOption = new AvatarOption();
        avatarOption.setMaxFileSize(5 * 1024); // ~500KB
        avatarOption.setMaxWidth(1000);
        avatarOption.setMaxHeight(1000);
        avatarOption.setCreatedBy("admin");
        avatarOption.setUpdatedBy("admin");
        genericService.saveEntity(avatarOption);
        logger.info("Avatar option created.");
    }

    private void createBadgeDefault() {
        badgeService.createBadge();
        badgeService.createTraineeBadge();
        badgeService.createBronzeBadge();
        badgeService.createSilverBadge();
        badgeService.createGoldBadge();
        badgeService.createPlatinumBadge();
        logger.info("Badge default created.");
    }
}
