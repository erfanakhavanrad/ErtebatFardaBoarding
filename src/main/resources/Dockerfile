FROM redis:6.0

# Disable Transparent Huge Pages (THP)
RUN echo 'echo madvise > /sys/kernel/mm/transparent_hugepage/enabled' >> /usr/local/bin/docker-entrypoint.sh \
    && chmod +x /usr/local/bin/docker-entrypoint.sh