#!/usr/bin/env node

const count = parseInt(process.argv[2] || 10);

const serialNo = (function* () {

  let serialNo = 0;

  while (true) {
    serialNo = serialNo % 1000000;
    yield serialNo++;
  }

})();

const generate = () => (
  (BigInt(Date.now()) * 1000000n + BigInt(serialNo.next().value)).toString(36)
  + ('000' + Math.floor(Math.random() * 10000).toString(36)).slice(-4)
).toUpperCase();

for (let i = 0; i < count; i++) {
  console.log(generate());
}
