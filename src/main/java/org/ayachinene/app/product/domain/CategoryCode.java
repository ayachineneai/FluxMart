package org.ayachinene.app.product.domain;

import org.ayachinene.utils.Validates;

public record CategoryCode(String value) {

    public CategoryCode {
        value = Validates.requiredText(value, "categoryCode");
    }
}
