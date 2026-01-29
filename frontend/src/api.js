/**
 * API helper functions for making HTTP requests to the backend
 */

/**
 * Make a GET request
 * @param {string} path - API endpoint path (e.g., '/api/users')
 * @returns {Promise<any>} - Response data
 */
export async function getJson(path) {
  console.log('Fetching:', path);
  const response = await fetch(path, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    }
  });
  
  console.log('Response status:', response.status);
  
  if (!response.ok) {
    const error = await response.json();
    console.error('Error response:', error);
    throw new Error(error.error?.message || 'Request failed');
  }
  
  const data = await response.json();
  console.log('Response data:', data);
  return data;
}

/**
 * Make a POST request
 * @param {string} path - API endpoint path (e.g., '/api/deposit')
 * @param {object} body - Request body
 * @returns {Promise<any>} - Response data
 */
export async function postJson(path, body) {
  const response = await fetch(path, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(body)
  });
  
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.error?.message || 'Request failed');
  }
  
  return response.json();
}
