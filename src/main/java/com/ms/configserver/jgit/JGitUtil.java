package com.ms.configserver.jgit;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class JGitUtil {
	

	private static Log logger=LogFactory.getLog(JGitUtil.class);
	
	/**提交并上传
	 * @param repoDir
	 * @return
	 */
	public static String gitCommitAndPush(File repoDir,String user,String password) {
	    File RepoGitDir = new File(repoDir.getAbsolutePath() + "/.git");
	    if (!RepoGitDir.exists()) {
	        logger.info("Error! Not Exists : " + RepoGitDir.getAbsolutePath());
	    } else {
	        Repository repo = null;
	        Git git = null;
	        try {
	            repo = new FileRepository(RepoGitDir.getAbsolutePath());
	            git = new Git(repo);
	            //add
	            AddCommand add = git.add();
	            add.addFilepattern(".").call();
	            //commit
	            CommitCommand commit = git.commit();
//	            commit.setAuthor("lzredking","redkingyang@foxmail.com");
	            commit.setAll(true);
	            //git commit -m "use jgit"
	            RevCommit revCommit= commit.setMessage("spring cloud config jgit").call();
	            
	            String commitId = revCommit.getId().name();
	            System.out.println(commitId);
	            //push
	            PushCommand push = git.push();
	            //			
	            if(StringUtils.isNotBlank(user) && StringUtils.isNotBlank(password)) {
	            	push.setCredentialsProvider(new UsernamePasswordCredentialsProvider(user, password));
	            }
	            push.setPushAll();
	            push.call();//git pus
	            logger.info("Commited from remote repository to local repository at " + repo.getDirectory());
	            return revCommit.getFullMessage();
	        } catch (Exception e) {
	            logger.error(e.getMessage() + " : " + RepoGitDir.getAbsolutePath());
	        } finally {
	            if (repo != null) {
	                repo.close();
	            }
	            if(git != null) {
	            	git.close();
	            }
	            	
	        }
	    }
		return null;
	}
	/** pull 拉取文件
	 * @param repoDir
	 */
	public static boolean gitPull(File repoDir,String user,String password) {
	    File RepoGitDir = new File(repoDir.getAbsolutePath() + "/.git");
	    if (!RepoGitDir.exists()) {
	        logger.info("Error! Not Exists : " + RepoGitDir.getAbsolutePath());
	    } else {
	        Repository repo = null;
	        Git git = null;
	        try {
	            repo = new FileRepository(RepoGitDir.getAbsolutePath());
	            git = new Git(repo);
	            PullCommand pullCmd = git.pull();
	            //
	            if(StringUtils.isNotBlank(user) && StringUtils.isNotBlank(password)) {
	            	pullCmd.setCredentialsProvider(new UsernamePasswordCredentialsProvider(user, password));
	            }
	            PullResult presult= pullCmd.call();
	            
	            logger.info("Pulled from remote repository to local repository at " + repo.getDirectory());
	            return presult.isSuccessful();
	        } catch (Exception e) {
	            logger.info(e.getMessage() + " : " + RepoGitDir.getAbsolutePath());
	        } finally {
	            if (repo != null) {
	                repo.close();
	            }
	            if(git != null) {
	            	git.close();
	            }
	        }
	    }
		return false;
	}
	
	public static String gitFetch(File repoDir,String user,String password) {
	    File RepoGitDir = new File(repoDir.getAbsolutePath() + "/.git");
	    if (!RepoGitDir.exists()) {
	        logger.info("Error! Not Exists : " + RepoGitDir.getAbsolutePath());
	    } else {
	        Repository repo = null;
	        Git git = null;
	        try {
	            repo = new FileRepository(RepoGitDir.getAbsolutePath());
	            git = new Git(repo);
	            FetchCommand pullCmd = git.fetch();
	            //
	            if(StringUtils.isNotBlank(user) && StringUtils.isNotBlank(password)) {
	            	pullCmd.setCredentialsProvider(new UsernamePasswordCredentialsProvider(user, password));
	            }
//	            pullCmd.setRemote(remote)
	            FetchResult presult= pullCmd.call();
	            
	            logger.info("FetchCommand from remote repository to local repository at " + repo.getDirectory());
	            return presult.getMessages();
	        } catch (Exception e) {
	            logger.info(e.getMessage() + " : " + RepoGitDir.getAbsolutePath());
	        } finally {
	            if (repo != null) {
	                repo.close();
	            }
	            if(git != null) {
	            	git.close();
	            }
	        }
	    }
		return null;
	}
	
	public static String gitClone(String remoteUrl, File repoDir) {
		String result=null;
	    try {
	        Git git = Git.cloneRepository()
	                .setURI(remoteUrl)
	                .setDirectory(repoDir)
	                .call();

	        logger.info("Cloning from " + remoteUrl + " to " + git.getRepository());
	    } catch (Exception e) {
	        logger.error(e.getMessage());
	        result=e.getMessage();
	    }
		return result;
	}
	
}
