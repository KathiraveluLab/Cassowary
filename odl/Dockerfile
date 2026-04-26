FROM eclipse-temurin:17-jre-jammy

ARG ODL_VERSION=0.17.3
ENV ODL_VERSION=${ODL_VERSION}

RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    netcat-openbsd \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /opt

RUN curl -fsSL \
    "https://nexus.opendaylight.org/content/repositories/opendaylight.release/org/opendaylight/integration/karaf/${ODL_VERSION}/karaf-${ODL_VERSION}.tar.gz" \
    -o karaf.tar.gz \
 && tar -xzf karaf.tar.gz \
 && mv karaf-${ODL_VERSION} opendaylight \
 && rm karaf.tar.gz

# Append odl-restconf and odl-mdsal-all to featuresBoot so they
# are installed automatically on every container startup.
RUN sed -i 's/^featuresBoot\s*=\s*/featuresBoot = odl-restconf,odl-mdsal-all,/' \
    /opt/opendaylight/etc/org.apache.karaf.features.cfg

EXPOSE 8181 8101 6633 6640

CMD ["/opt/opendaylight/bin/karaf", "server"]
