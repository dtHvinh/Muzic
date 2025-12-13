/**
 * @typedef {Object} Area
 * @property {string} id
 * @property {string} type
 * @property {string} name
 * @property {string|null} type_id
 * @property {string|null} sort_name
 * @property {Object|null} life_span
 */

/**
 * @typedef {Object} Alias
 * @property {string} name
 * @property {string|null} locale
 * @property {string|null} type
 * @property {boolean|null} primary
 * @property {string|null} sort_name
 * @property {string|null} type_id
 * @property {string|null} begin_date
 * @property {string|null} end_date
 */

/**
 * @typedef {Object} Tag
 * @property {number} count
 * @property {string} name
 */

/**
 * @typedef {Object} LifeSpan
 * @property {string} begin
 * @property {string|boolean} [ended]     // MusicBrainz sends "true"/"false" as string or boolean!
 */

/**
 * Full MusicBrainz Artist object
 * @typedef {Object} Artist
 * @property {string} id
 * @property {string} type                    // "Person", "Group", etc.
 * @property {number} score
 * @property {string} name
 * @property {string} [gender]                // "male", "female", or undefined
 * @property {string} [country]               // "US", "GB", etc.
 * @property {Area} area
 * @property {Area} [begin-area]
 * @property {Area} [end-area]
 * @property {string} [disambiguation]
 * @property {string[]} [ipis]
 * @property {string[]} [isnis]
 * @property {Alias[]} [aliases]
 * @property {Tag[]} [tags]
 * @property {string} type-id
 * @property {string} [gender-id]
 * @property {string} sort-name
 * @property {LifeSpan} life-span
 */
