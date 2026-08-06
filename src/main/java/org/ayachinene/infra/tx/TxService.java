package org.ayachinene.infra.tx;

import org.ayachinene.app.service.Tx;
import org.ayachinene.infra.exception.UncheckedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.Callable;

@Service
public class TxService implements Tx {

    private final TransactionTemplate txTemplate;
    private final TransactionTemplate consistentReadTxTemplate;

    public TxService(
            TransactionTemplate txTemplate,
            PlatformTransactionManager txManager
    ) {
        this.txTemplate = txTemplate;
        this.consistentReadTxTemplate = new TransactionTemplate(txManager, txTemplate);
        this.consistentReadTxTemplate.setReadOnly(true);
        this.consistentReadTxTemplate.setIsolationLevel(
                TransactionDefinition.ISOLATION_REPEATABLE_READ
        );
    }

    @Override
    public <T> T run(Callable<T> action) {
        return execute(txTemplate, action);
    }

    @Override
    public void run(Runnable action) {
        run(() -> {
            action.run();
            return null;
        });
    }

    public <T> T read(Callable<T> action) {
        return execute(consistentReadTxTemplate, action);
    }

    private <T> T execute(TransactionTemplate template, Callable<T> action) {
        return template.execute(status -> {
            try {
                return action.call();
            } catch (RuntimeException exception) {
                throw exception;
            } catch (Exception exception) {
                throw new UncheckedException(exception);
            }
        });
    }
}
