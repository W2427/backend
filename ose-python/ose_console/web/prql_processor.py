import prqlc
from typing import Tuple

def process_prql(prql_query: str) -> str:
    try:
        compiled = prqlc.compile(prql_query)
        return compiled
    except prqlc.PrqlError as e:
        raise ValueError(f"PRQL compilation error: {e}")
