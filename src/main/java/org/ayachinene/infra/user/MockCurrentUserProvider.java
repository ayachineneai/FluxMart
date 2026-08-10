package org.ayachinene.infra.user;

import org.ayachinene.app.user.CurrentUserProvider;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.shared.uuid7.UUID7s;
import org.springframework.stereotype.Component;

@Component
public class MockCurrentUserProvider implements CurrentUserProvider {

    private static final UUID7 MOCK_USER_ID = UUID7s.fromStringUnsafe(
            "0195d7d2-6380-7a5c-8b35-3a23b8df1f00"
    );

    @Override
    public UUID7 currentUserId() {
        return MOCK_USER_ID;
    }
}
