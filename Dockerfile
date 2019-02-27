FROM odavid/my-bloody-jenkins:latest

COPY config.yml /config.yml
COPY bin/entrypoint.sh /usr/bin/entrypoint.sh

COPY --chown=jenkins:jenkins custom-plugins/* /usr/share/jenkins/ref/plugins/
COPY --chown=jenkins:jenkins dsl /tmp/dsl/