function getLocalMonth(monthIndex) {
  const date = new Date(2024, monthIndex);
  return new Intl.DateTimeFormat(undefined, { month: 'long' }).format(date);
}

if (typeof globalThis !== 'undefined') {
  globalThis.getLocalMonth = getLocalMonth;
} else if (typeof window !== 'undefined') {
  window.getLocalMonth = getLocalMonth;
} else if (typeof global !== 'undefined') {
  global.getLocalMonth = getLocalMonth;
}

function allocate(instance, str) {
  const encoder = new TextEncoder();
  const encodedStr = encoder.encode(str);

  // usage `allocate` from  WebAssembly
  const ptr = instance.exports.allocate(encodedStr.length + 1);

  // get memory from WebAssembly
  const memory = new Uint8Array(instance.exports.memory.buffer);
  memory.set(encodedStr, ptr);
  memory[ptr + encodedStr.length] = 0;

  return ptr; // return ptr
}

// init wasm module
async function loadWasm() {
  const response = await fetch('your_module.wasm');
  const buffer = await response.arrayBuffer();

  const { instance } = await WebAssembly.instantiate(buffer, {
    env: {
      getLocalMonth: (monthIndex) => {
        const monthName = getLocalMonth(monthIndex);
        console.log("Locale month name：", monthName);
        return allocate(instance, monthName);
      }
    }
  });

  instance.exports.printMonthName(0);
}

loadWasm();