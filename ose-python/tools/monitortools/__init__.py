# coding: utf-8

import time
import logging


logger = logging.getLogger(__name__)


def timeit(method):
    def timed(*args, **kw):
        ts = time.time()
        result = method(*args, **kw)
        te = time.time()
        logger.info("method %s use seconds:%.2f", method, te - ts)
        return result
    return timed

