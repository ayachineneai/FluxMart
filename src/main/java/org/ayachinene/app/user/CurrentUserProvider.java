package org.ayachinene.app.user;

import org.ayachinene.shared.uuid7.UUID7;

public interface CurrentUserProvider {

    UUID7 currentUserId();
}
