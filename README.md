To deploy to Maven central repository follow the below steps

1. Login to http://oss.sonatype.org with your account/password.
2. *(One time only)* Select *Profile/User Token* and click on *Access User Token*. You will need the info in the next step
2. *(One time only)*  Make sure you added to your Maven settings.xml 

	            <server>
                        <id>sonatype-nexus-snapshots</id>
                        <username>user-token</username>
                        <password>user-token</password>
                </server>
                <server>
                        <id>sonatype-nexus-staging</id>
                        <username>beradrian</username>
                        <password>${sonatype.nexus.password}</password>
                </server>

3. *(One time only)* Make sure you have a GPG key generated and uploaded to a key server. See [here](http://blog.sonatype.com/2010/01/how-to-generate-pgp-signatures-with-maven) for more details.
4. Run `mvn -Dsonatype.nexus.password=<your password> -DperformRelease=true -Dgpg.passphrase=<your.passphrase> deploy`
5. Go to *Staging Repositories* and select the newly created repository called something like *netsfjcommon-XXXX*
6. Select the repository and click *Close*. This could take a while.
7. Refresh the page and after the repository is closed, select it and click *Release*
8. That's it. Updates to https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22net.sf.jcommon%22 can take up to 2 hours.