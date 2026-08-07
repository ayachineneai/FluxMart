package org.ayachinene.app.domain.product;

import org.ayachinene.utils.Validates;

public record CategoryCode(String value) {

    public CategoryCode {
        value = Validates.requiredText(value, "categoryCode");
    }
}
